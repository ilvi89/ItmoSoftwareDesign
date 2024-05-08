package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

@Data
@Builder
public class BadgeDataResponse {
    private Long id;
    private BadgeData data;
}
