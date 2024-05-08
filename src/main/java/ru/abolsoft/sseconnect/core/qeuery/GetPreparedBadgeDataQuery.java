package ru.abolsoft.sseconnect.core.qeuery;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetPreparedBadgeDataQuery {
    private final CoreServicePort coreServicePort;

    public Res execute(Req req) {
        Optional<BadgeData> badgeData = coreServicePort.getBadgeDataById(req.badgeId);
        if (badgeData.isEmpty())
            throw new NotImplemented();

        return new Res(badgeData.get());
    }

    @Data
    @Builder
    public static class Req {
        private Long badgeId;
    }

    @Data
    @Builder
    public static class Res {
        private BadgeData badgeData;
    }
}
