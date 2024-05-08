package ru.abolsoft.sseconnect.base.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.Map;

@Getter
public class LocalizedName implements ValueObject {
    private final Map<Language, String> translations;

    public LocalizedName(Map<Language, String> translations) {
        this.translations = translations;
    }

    @JsonIgnore
    public String get(Language l) {
        return translations.get(l);
    }

    public static LocalizedName of(Map<Language, String> translations) {
        return new LocalizedName(translations);
    }
}

