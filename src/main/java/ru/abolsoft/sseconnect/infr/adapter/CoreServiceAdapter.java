package ru.abolsoft.sseconnect.infr.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.*;
import okhttp3.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.core.port.res.Property;
import ru.abolsoft.sseconnect.infr.adapter.utils.retry.RetryHandler;
import ru.abolsoft.sseconnect.infr.adapter.utils.retry.RetryWithBackoff;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoreServiceAdapter implements CoreServicePort, RetryHandler {
    @Value("${app.core.superuser.username}")
    private String superuserUsername;
    @Value("${app.core.superuser.password}")
    private String superuserPassword;

    @Setter(AccessLevel.PROTECTED)
    private String superuserToken = Strings.EMPTY;

    private final ObjectMapper objectMapper;

    @Override
    @RetryWithBackoff(maxAttempts = 5, delay = 1000, multiplier = 2)
    public Optional<Badge> getBadgeForMember(Long memberId, BadgePreset badgePreset) {

        var filterParams = badgePreset
                .getProperties().entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> FilterParamDTO.builder()
                        .filterAlias(entry.getKey())
                        .filterValue(entry.getValue())
                        .build()
                )
                .toList();
        var filter = FilterParamsDTO.builder()
                .filterParams(filterParams)
                .build();
        String filterJson = null;
        try {
            filterJson = objectMapper.writeValueAsString(filter);
        } catch (JsonProcessingException e) {
            throw new NotImplemented();
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(filterJson, mediaType);

        Request request = new Request.Builder()
                .url("http://158.160.165.207:8080/api/core/entity/mkf/list?type=" + badgePreset.getAlias() + "&page=0&size=10")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", superuserToken)
                .build();

        ObjectsResponseDTO objectsResponse = ObjectsResponseDTO.builder().build();
        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                this.login();
                throw new NotImplemented();
            }

            var responseBody = Objects.requireNonNull(response.body()).string();
            objectsResponse = objectMapper.readValue(responseBody, ObjectsResponseDTO.class);
            var filteredObjects = objectsResponse.getEntities().stream().filter(objectDTO -> objectDTO.getParent() == memberId).toList();
            objectsResponse.setEntities(filteredObjects);
            objectsResponse.setTotalCount(filteredObjects.size());

            response.close();

            if (objectsResponse.getTotalCount() != 1)
                throw new NotImplemented();
            var object = objectsResponse.getEntities().get(0);
            var badge = Badge.create(UuidCreator.getTimeBased(), (long) object.getId(), (long) object.getParent());
            return Optional.of(badge);
        } catch (IOException e) {
            throw new NotImplemented();
        }


    }

    @Override
    @RetryWithBackoff(maxAttempts = 5, delay = 1000, multiplier = 2)
    public Optional<BadgeData> getBadgeDataById(Long badgeId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("http://158.160.165.207:8080/api/core/entity/mkf/" + badgeId)
                .get()
                .addHeader("Cookie", superuserToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            var responseBody = Objects.requireNonNull(response.body()).string();
            var object = objectMapper.readValue(responseBody, ObjectDTO.class);
            response.close();

            var props = object.getValues()
                    .entrySet()
                    .stream()
                    .map(entry -> Property.create(entry.getKey(), entry.getValue()))
                    .toList();

            var data = BadgeData.create(props);
            return Optional.of(data);

        } catch (IOException e) {
            throw new NotImplemented();
        }
    }


    private void login() {
        var loginRes = UserCredentialsDTO.builder()
                .username(superuserUsername)
                .password(superuserPassword)
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String loginResJson;
        try {
            loginResJson = objectMapper.writeValueAsString(loginRes);
        } catch (JsonProcessingException e) {
            throw new NotImplemented();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(loginResJson, mediaType);
        Request request = new Request.Builder()
                .url("http://158.160.165.207:8080/api/core/auth/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            var token = response.headers("Set-Cookie");
            var accessToken = token.stream().filter(string -> string.contains("ekit-access")).findFirst().get();
            this.setSuperuserToken(accessToken);
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void executeAfterFirstRetry() {
        this.login();
    }

    @Override
    public void executeBeforeRetry() {

    }

    @Override
    public void executeAfterRetry() {

    }
}

@Data
@Builder
class UserCredentialsDTO {
    private String username;
    private String password;
}


@Data
@Builder
class FilterParamsDTO {
    @JsonProperty("filter_params")
    private List<FilterParamDTO> filterParams;
}

@Data
@Builder
class FilterParamDTO {
    @JsonProperty("filter_alias")
    private String filterAlias;
    @JsonProperty("filter_value")
    private String filterValue;
}


@Data
@Builder
class ObjectsResponseDTO {
    @JsonProperty("total_count")
    private int totalCount;
    private List<ObjectDTO> entities;
}

@Data
class ObjectDTO {
    private int id;
    private int parent;
    private String type;
    private boolean deleted;
    private String created;
    private String updated;
    private Map<String, String> values;
}
