package ru.abolsoft.sseconnect.infr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.infr.repository.model.BadgeModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BadgeRepositoryJpa extends JpaRepository<BadgeModel, UUID> {
    @Query(value = "select b.status from BadgeModel b where b.id = :id")
    Optional<Status> findStatusById(@Param("id") UUID badgeId);
}
