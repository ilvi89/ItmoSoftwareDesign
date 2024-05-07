package ru.abolsoft.sseconnect.base.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class BaseAggregate<T extends Serializable> extends BaseEntity<T> implements Aggregate<T> {
}
