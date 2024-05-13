package ru.abolsoft.sseconnect.core.entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.abolsoft.sseconnect.infr.repository.model.DeskModel;

@Mapper(componentModel = "spring")
public interface DeskMapper {
    DeskModel toModel(Desk desk);
    Desk toEntity(DeskModel deskModel);
}
