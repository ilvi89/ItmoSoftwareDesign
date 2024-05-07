package ru.abolsoft.sseconnect.infr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.infr.repository.model.DeskModel;

@Repository
public interface DeskRepositoryJpa extends JpaRepository<DeskModel, Long> {

}
