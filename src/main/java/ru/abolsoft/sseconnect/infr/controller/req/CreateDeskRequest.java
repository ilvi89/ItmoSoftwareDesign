package ru.abolsoft.sseconnect.infr.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateDeskRequest {
    @Schema(description = "Имя новой стойки")
    private String name;
}
