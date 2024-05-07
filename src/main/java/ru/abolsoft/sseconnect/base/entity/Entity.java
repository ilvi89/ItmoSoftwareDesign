package ru.abolsoft.sseconnect.base.entity;

import lombok.Getter;

import java.io.Serializable;

public interface Entity<T extends Serializable> {
    T getId();
    void setId(T id);
}

