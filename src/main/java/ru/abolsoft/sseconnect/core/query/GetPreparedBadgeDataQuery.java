package ru.abolsoft.sseconnect.core.query;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.core.repository.BadgePresetRepository;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPreparedBadgeDataQuery {
    private final CoreServicePort coreServicePort;
    private final BadgePresetRepository badgePresetRepository;
    private final BadgeRepository badgeRepository;

    public Res execute(Req req) {
        //TODO: add async
        var badge = badgeRepository.findById(req.badgeId);
        if (badge.isEmpty())
            throw new NotImplemented();

        Optional<BadgeData> badgeData = coreServicePort.getBadgeDataById(badge.get().getCustomObjectId());
        var optionalPreset = badgePresetRepository.findById(req.badgePresetId);
        if (optionalPreset.isEmpty())
            throw new NotImplemented();

        if (badgeData.isEmpty())
            throw new NotImplemented();


        BadgeData badgeDataWithPreset = badgeData.get().mapByPreset(optionalPreset.get());
        return new Res(badgeDataWithPreset);
    }

    @Data
    @Builder
    public static class Req {
        private UUID badgeId;
        private UUID badgePresetId;
    }

    @Data
    @Builder
    public static class Res {
        private BadgeData badgeData;
    }
}
