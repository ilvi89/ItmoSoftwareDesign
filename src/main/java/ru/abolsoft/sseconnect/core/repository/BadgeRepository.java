package ru.abolsoft.sseconnect.core.repository;

import ru.abolsoft.sseconnect.base.entity.BaseRepository;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.Status;

import java.util.Optional;
import java.util.Set;

public interface BadgeRepository extends BaseRepository<Badge, Long> {

    Optional<Status> findStatusById(Long badgeId);
}


