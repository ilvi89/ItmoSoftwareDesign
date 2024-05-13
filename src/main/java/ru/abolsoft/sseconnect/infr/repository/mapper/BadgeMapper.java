package ru.abolsoft.sseconnect.infr.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.infr.repository.model.BadgeModel;

@Mapper(componentModel = "spring")
public interface BadgeMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customObjectId", source = "customObjectId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "desk.id", source = "desk.id")
    @Mapping(target = "desk.name", source = "desk.name")
    @Mapping(target = "desk.mobile", source = "desk.mobile")
    @Mapping(target = "memberId", source = "ownerId")
    BadgeModel toModel(Badge badge);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customObjectId", source = "customObjectId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "desk", source = "desk")
    @Mapping(target = "ownerId", source = "memberId")
    Badge toEntity(BadgeModel badgeModel);
}
