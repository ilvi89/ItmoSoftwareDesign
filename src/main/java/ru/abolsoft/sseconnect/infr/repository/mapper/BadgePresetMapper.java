package ru.abolsoft.sseconnect.infr.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.infr.repository.model.BadgePresetModel;
import ru.abolsoft.sseconnect.infr.repository.model.BadgePresetPropertyModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface BadgePresetMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "properties", expression = "java(mapToBadgePresetPropertyModelSet(entity.getProperties()))")
    BadgePresetModel toModel(BadgePreset entity);

    @Mapping(target = "id", source = "model.id")
    @Mapping(target = "alias", source = "model.alias")
    @Mapping(target = "properties", expression = "java(mapToProperties(model.getProperties()))")
    BadgePreset toEntity(BadgePresetModel model);


    default Set<BadgePresetPropertyModel> mapToBadgePresetPropertyModelSet(Map<String, String> properties) {
        if (properties == null) {
            return null;
        }
        return properties.entrySet().stream()
                .map(entry -> {
                    BadgePresetPropertyModel propertyModel = new BadgePresetPropertyModel();
                    propertyModel.setKey(entry.getKey());
                    propertyModel.setValue(entry.getValue()); // Assuming value is set in BadgePresetPropertyModel
                    return propertyModel;
                })
                .collect(Collectors.toSet());
    }

    default Map<String, String> mapToProperties(Set<BadgePresetPropertyModel> propertyModels) {
        if (propertyModels == null) {
            return null;
        }
        Set<Map.Entry<String, String>> res = propertyModels.stream()
                .map(model -> {
                    if (model.getValue() == null)
                        return Map.entry(model.getKey(), "");
                    else return Map.entry(model.getKey(), model.getValue());
                })
                .collect(Collectors.toSet());
        var map = new HashMap<String, String>();
        res.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return map;
    }
}
