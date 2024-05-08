package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;

@Service
@RequiredArgsConstructor
public class BadgeAcceptUseCase {
    private final BadgeRepository badgeRepository;

    public Res execute(Req req) {
        var optionalBadge = badgeRepository.findById(req.getBadgeId());
        if (optionalBadge.isEmpty())
            throw new NotImplemented();
        var badge = optionalBadge.get();
        badge.stopProcessing();
        badge = badgeRepository.save(badge);
        return new Res(badge.getId(), badge.getDesk().getId(), badge.getStatus());
    }

    @Data
    @Builder
    public static class Req {
        private Long badgeId;
    }

    @Data
    @Builder
    public static class Res {
        private Long badgeId;
        private Long deskId;
        private Status status;
    }
}
