package ru.abolsoft.sseconnect.infr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI applicationOpenApi() throws IOException {
        return new OpenAPI()
                .addServersItem(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                )
                .info(
                        new Info()
                                .title("Badge module API")
                );
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }
}
