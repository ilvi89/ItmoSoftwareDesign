package ru.abolsoft.sseconnect.core.entity;

import lombok.Getter;

@Getter
public enum Status {
    NEW("new"),
    IN_PROCESS("in_process"),
    ACCEPTED("accepted"),
    BLOCKED("blocked");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status of(String status) {
        for (Status s : values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}