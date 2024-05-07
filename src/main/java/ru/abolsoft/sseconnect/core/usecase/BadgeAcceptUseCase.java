package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.BadgeData;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgeAcceptUseCase {
    private final BadgeRepository badgeRepository;
    private final CoreServicePort coreServicePort;

    public Res execute(Req req) {

        var optionalBadge = badgeRepository.findById(req.getBadgeId());
        if (optionalBadge.isEmpty())
            throw new NotImplemented();
        var badge = optionalBadge.get();
        badge.stopProcessing();
        badge = badgeRepository.save(badge);

        Optional<BadgeData> badgeData = coreServicePort.getBadgeDataById(badge.getId());
        if (badgeData.isEmpty())
            throw new NotImplemented();
        badgeData.get().setBadge(badge);
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
