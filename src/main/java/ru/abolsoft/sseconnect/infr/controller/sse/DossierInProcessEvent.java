package ru.abolsoft.sseconnect.infr.controller.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeResponse;

@Data
@AllArgsConstructor
public class DossierInProcessEvent {
    private BadgeResponse badge;

    public static String EVENT_NAME = "dossier_in_process_event";
}
