package ru.abolsoft.sseconnect.infr.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.entity.DeskMapper;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;
import ru.abolsoft.sseconnect.infr.repository.mapper.BadgeMapper;
import ru.abolsoft.sseconnect.infr.repository.model.BadgeModel;
import ru.abolsoft.sseconnect.infr.repository.model.DeskModel;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BadgeRepositoryImpl implements BadgeRepository {
    private final BadgeRepositoryJpa badgeRepositoryJpa;
    private final BadgeMapper badgeMapper;
    private final DeskMapper deskMapper;


    @Override
    public Optional<Status> findStatusById(UUID badgeId) {
        return badgeRepositoryJpa.findStatusById(badgeId);
    }

    @Override
    public boolean existByDeskIdAndStatus(Long deskId, Status status) {
        return badgeRepositoryJpa.existsByDeskIdAndStatus(deskId, status);
    }

    @Override
    public Optional<Badge> findById(UUID id) {
        return badgeRepositoryJpa.findById(id).map(badgeMapper::toEntity);
    }

    @Override
    public Badge save(Badge entity) {
        BadgeModel model = badgeMapper.toModel(entity);
        model = badgeRepositoryJpa.save(model);
        return badgeMapper.toEntity(model);
    }
}
