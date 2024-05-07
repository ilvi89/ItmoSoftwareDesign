package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.BadgeData;

@Data
@Builder
public class AcceptBadgeResponse {
    private BadgeData badge;
}
