package ru.abolsoft.sseconnect.infr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.abolsoft.sseconnect.infr.repository.model.BadgePresetModel;

import java.util.Optional;
import java.util.UUID;

public interface BadgePresetRepositoryJpa extends JpaRepository<BadgePresetModel, UUID> {
}



