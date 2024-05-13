package ru.abolsoft.sseconnect.core.port;

import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;

import java.util.Optional;

public interface CoreServicePort {
    Optional<Badge> getBadgeForMember(Long memberId, BadgePreset badgePreset);
    Optional<BadgeData> getBadgeDataById(Long badgeId);
}
