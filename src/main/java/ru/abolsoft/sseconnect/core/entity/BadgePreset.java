package ru.abolsoft.sseconnect.core.entity;

import lombok.Getter;
import lombok.Setter;
import ru.abolsoft.sseconnect.base.entity.BaseEntity;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class BadgePreset extends BaseEntity<UUID> {
    private String alias;
    private Map<String, String> properties;
}
