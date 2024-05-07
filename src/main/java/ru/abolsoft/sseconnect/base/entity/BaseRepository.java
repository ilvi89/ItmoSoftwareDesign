package ru.abolsoft.sseconnect.base.entity;

import ru.abolsoft.sseconnect.core.entity.Badge;

import java.io.Serializable;
import java.util.Optional;

public interface BaseRepository<TEntity extends Entity<TID>, TID extends Serializable> {
    Optional<TEntity> findById(TID id);

    TEntity save(TEntity entity);
}
