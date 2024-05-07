package ru.abolsoft.sseconnect.infr.controller.res;

import lombok.Builder;
import lombok.Data;
import ru.abolsoft.sseconnect.core.entity.Status;

@Data
@Builder
public class DeskResponse {
    private Long id;
    private String name;
    private Boolean mobileConnection;
}


