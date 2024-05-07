package ru.abolsoft.sseconnect.base.entity;

import lombok.Getter;

@Getter
public enum Language {
    RU("ru"),
    EN("en");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public static Language of(String code) {
        for (Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("No language with code " + code + " found");
    }
}
