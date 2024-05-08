package ru.abolsoft.sseconnect.core.port.res;

import lombok.Getter;

@Getter
public enum PropertyType {
    STRING("string"),
    INTEGER("integer"),
    BOOLEAN("boolean");

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    public static PropertyType of(String type) {
        for (PropertyType propertyType : values()) {
            if (propertyType.value.equalsIgnoreCase(type)) {
                return propertyType;
            }
        }
        throw new IllegalArgumentException("Unknown property type: " + type);
    }
}
