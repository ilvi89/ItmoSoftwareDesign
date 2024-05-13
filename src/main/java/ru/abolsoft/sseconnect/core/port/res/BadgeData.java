package ru.abolsoft.sseconnect.core.port.res;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;

import java.util.List;
import java.util.Set;

@Getter
@Setter(AccessLevel.PROTECTED)
public class BadgeData {
    private List<Property> properties;


    public static BadgeData create(List<Property> properties) {
        var b = new BadgeData();
        b.setProperties(properties);
        return b;
    }

    public BadgeData mapByPreset(BadgePreset badgePreset) {
        BadgeData d = new BadgeData();
        List<Property> allProps = this.properties;
        Set<String> validProps = badgePreset.getProperties().keySet();
        allProps = allProps.stream().filter(property -> validProps.contains(property.getAlias())).toList();
        d.setProperties(allProps);
        return d;
    }
}

