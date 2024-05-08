package ru.abolsoft.sseconnect.infr.adapter;

import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.base.entity.Language;
import ru.abolsoft.sseconnect.base.entity.LocalizedName;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.port.CoreServicePort;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.core.port.res.Property;
import ru.abolsoft.sseconnect.core.port.res.PropertyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CoreServiceAdapter implements CoreServicePort {
    @Override
    public Optional<Badge> getBadgeForMember(Long memberId) {
        return Optional.of(Badge.create(memberId, memberId));
    }

    @Override
    public Optional<BadgeData> getBadgeDataById(Long badgeId) {

        var namePropsName = new HashMap<Language, String>();
        namePropsName.put(Language.EN, "Name");
        namePropsName.put(Language.RU, "Имя");
        var defaultNameValue = "";
        var nameValue = "Ilya";
        var props = new ArrayList<Property>();
        props.add(Property.create("name", PropertyType.STRING, LocalizedName.of(namePropsName), defaultNameValue, nameValue));
        var data = BadgeData.create(props);
        return Optional.of(data);
    }
}
