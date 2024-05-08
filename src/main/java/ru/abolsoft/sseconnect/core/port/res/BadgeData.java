package ru.abolsoft.sseconnect.core.port.res;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class BadgeData {
    private List<Property> properties;


    public static BadgeData create(List<Property> properties) {
        var b = new BadgeData();
        b.setProperties(properties);
        return b;
    }
}

