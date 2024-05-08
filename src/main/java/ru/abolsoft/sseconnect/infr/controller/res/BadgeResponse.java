package ru.abolsoft.sseconnect.infr.controller.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

@Data
@Builder
public class BadgeResponse {
    @Schema(description = "Id бейджа")
    private Long id;
    @Schema(description = "Статус бейджа")
    private Status status;
    @Schema(description = "Id стойки")
    private Long deskId;
}


