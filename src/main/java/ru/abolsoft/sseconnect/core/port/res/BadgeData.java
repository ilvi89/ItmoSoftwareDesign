package ru.abolsoft.sseconnect.core.port.res;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.Property;
import ru.abolsoft.sseconnect.core.entity.Status;

import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class BadgeData {
    private Long id;
    private List<Property> properties;
    private Long ownerId;
    private Long deskId;
    private Status status;


    public static BadgeData create(Long badgeId, List<Property> properties) {
        var b = new BadgeData();
        b.setId(badgeId);
        b.setProperties(properties);
        return b;
    }

    public void setBadge(Badge badge) {
        setOwnerId(badge.getOwnerId());
        setDeskId(badge.getDesk().getId());
        setStatus(badge.getStatus());
    }
}

