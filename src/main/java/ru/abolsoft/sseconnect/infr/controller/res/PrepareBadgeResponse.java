package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.BadgeData;
import ru.abolsoft.sseconnect.core.entity.Status;

@Data
@Builder
public class PrepareBadgeResponse {
    private Long badgeId;
    private Status status;
}



