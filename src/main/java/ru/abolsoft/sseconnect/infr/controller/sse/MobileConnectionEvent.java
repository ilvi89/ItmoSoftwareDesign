package ru.abolsoft.sseconnect.infr.controller.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeResponse;
import ru.abolsoft.sseconnect.infr.controller.res.DeskResponse;

@Data
@AllArgsConstructor
public class MobileConnectionEvent {
    private DeskResponse desk;

    public static String EVENT_NAME = "mobile_connection_event";
}
