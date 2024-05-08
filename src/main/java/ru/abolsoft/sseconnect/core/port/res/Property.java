package ru.abolsoft.sseconnect.core.port.res;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.abolsoft.sseconnect.base.entity.LocalizedName;
import ru.abolsoft.sseconnect.base.entity.ValueObject;

@Getter
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Property implements ValueObject {
    private final String alias;
    private final PropertyType type;

    private final LocalizedName name;
    private final String defaultValue;
    private final String value;


    public static Property create(String alias, PropertyType type, LocalizedName name, String defaultValue, String value) {
        return new Property(alias, type, name, defaultValue, value);
    }
}


