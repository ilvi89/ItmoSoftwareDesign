package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.Status;

@Data
@Builder
public class BadgeStatusResponse {
    private Long id;
    private Status status;
}
