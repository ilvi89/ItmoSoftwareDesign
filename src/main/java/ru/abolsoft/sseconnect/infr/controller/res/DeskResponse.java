package ru.abolsoft.sseconnect.infr.controller.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.Status;

@Data
@Builder
public class DeskResponse {
    @Schema(description = "Id стойки")
    private Long id;
    @Schema(description = "Имя стойки")
    private String name;
    @Schema(description = "Статус подключенного моб. приложения")
    private Boolean mobileConnection;
}


