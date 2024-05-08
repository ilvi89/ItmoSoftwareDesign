package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

@Data
@Builder
public class BadgeResponse {
    private Long id;
    private Status status;
    private Long deskId;
}


