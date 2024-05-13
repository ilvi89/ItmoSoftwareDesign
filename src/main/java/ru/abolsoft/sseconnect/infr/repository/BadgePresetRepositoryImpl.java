package ru.abolsoft.sseconnect.infr.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.core.repository.BadgePresetRepository;
import ru.abolsoft.sseconnect.infr.repository.mapper.BadgePresetMapper;
import ru.abolsoft.sseconnect.infr.repository.model.BadgePresetModel;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BadgePresetRepositoryImpl implements BadgePresetRepository {
    private final BadgePresetRepositoryJpa badgePresetRepository;
    private final BadgePresetMapper badgePresetMapper;

    @Override
    @Cacheable(value = "badgePresets", key = "#id")
    public Optional<BadgePreset> findById(UUID id) {
        Optional<BadgePresetModel> model = badgePresetRepository.findById(id);
        return model.map(badgePresetMapper::toEntity);
    }

    @Override
    public BadgePreset save(BadgePreset entity) {
        var model = badgePresetMapper.toModel(entity);
        model = badgePresetRepository.save(model);
        return badgePresetMapper.toEntity(model);
    }
}
