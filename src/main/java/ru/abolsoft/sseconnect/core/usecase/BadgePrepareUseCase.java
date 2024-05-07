package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;
import ru.abolsoft.sseconnect.core.repository.DeskRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgePrepareUseCase {
    private final BadgeRepository badgeRepository;
    private final DeskRepository deskRepository;
    private final CoreServicePort coreServicePort;

    public Res execute(Req req) {
        Optional<Badge> optionalBadge = coreServicePort.getBadgeForMember(req.memberId);
        if (optionalBadge.isEmpty())
            throw new NotImplemented();

        Optional<Desk> optionalDesk = deskRepository.findById(req.deskId);
        if (optionalDesk.isEmpty())
            throw new NotImplemented();


        Badge badge = optionalBadge.get();
        Desk desk = optionalDesk.get();

        Status badgeStatus = badgeRepository.findStatusById(badge.getId())
                .orElse(Status.NEW);
        badge.chaneStatus(badgeStatus);

        badge.startProcessing(desk);
//        TODO: some processing
//        badge.stopProcessing();

        badge = badgeRepository.save(badge);
        var d = badge.getDesk();


        return new Res(badge.getId(), d.getId(), badge.getStatus());

    }


    @Data
    @Builder
    public static class Req {
        private Long memberId;
        private Long deskId;
    }

    @Data
    @Builder
    public static class Res {
        private Long badgeId;
        private Long deskId;
        private Status status;
    }
}
