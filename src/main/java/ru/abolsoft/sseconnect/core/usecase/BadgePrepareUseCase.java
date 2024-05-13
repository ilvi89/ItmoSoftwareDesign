package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.repository.BadgePresetRepository;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;
import ru.abolsoft.sseconnect.core.repository.DeskRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BadgePrepareUseCase {
    private final BadgeRepository badgeRepository;
    private final DeskRepository deskRepository;
    private final CoreServicePort coreServicePort;
    private final BadgePresetRepository badgePresetRepository;

    @Transactional(rollbackFor = Exception.class)
    public Res execute(Req req) {

        boolean badgesInProcessExist = badgeRepository.existByDeskIdAndStatus(req.deskId, Status.IN_PROCESS);
        if (badgesInProcessExist) {
            throw new NotImplemented();
        }

        Optional<BadgePreset> optionalPreset = badgePresetRepository.findById(req.presetId);
        if (optionalPreset.isEmpty())
            throw new NotImplemented();

        Optional<Badge> optionalBadge = coreServicePort.getBadgeForMember(req.memberId, optionalPreset.get());
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
        private UUID presetId;
        private Long memberId;
        private Long deskId;
    }

    @Data
    @Builder
    public static class Res {
        private UUID badgeId;
        private Long deskId;
        private Status status;
    }
}
