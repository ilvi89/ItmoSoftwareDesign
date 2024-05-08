package ru.abolsoft.sseconnect.infr.controller.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

@Data
@Builder
public class BadgeDataResponse {
    @Schema(description = "Id бейджа")
    private Long id;
    @Schema(description = "Динамические поля бейджа")
    private BadgeData data;
}
