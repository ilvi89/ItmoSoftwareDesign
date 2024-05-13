package ru.abolsoft.sseconnect.infr.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.core.port.res.Property;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoreServiceAdapter implements CoreServicePort {
    private final ObjectMapper objectMapper;
    private final String serviceAccountToken = "ekit-access=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNTYwODA2OCwiZXhwIjoxNzE1NjExNjY4fQ.6batpSu84T8BY8Z-9LKUHraHkgaHoDEn2AJSBUrx6CUBgWvPUHAVlXoWgYkvZKhz8fijx5MNFmjCDHHrDXh5zA; Path=/api/core; Max-Age=3600; Expires=Mon, 13 May 2024 14:47:48 GMT; HttpOnly";

    @Override
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
                .addHeader("Cookie", serviceAccountToken)
                .build();

        ObjectsResponseDTO objectsResponse = ObjectsResponseDTO.builder().build();
        try {
            Response response = client.newCall(request).execute();
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
    public Optional<BadgeData> getBadgeDataById(Long badgeId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("http://158.160.165.207:8080/api/core/entity/mkf/" + badgeId)
                .get()
                .addHeader("Cookie", serviceAccountToken)
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
