package ru.abolsoft.sseconnect.infr.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.repository.DeskRepository;
import ru.abolsoft.sseconnect.infr.repository.model.DeskModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DeskRepositoryImpl<T> implements DeskRepository {
    private final DeskRepositoryJpa deskRepositoryJpa;
    private final Map<Long, Optional<T>> map = new HashMap<>();


    @Override
    public Optional<Desk> findById(Long id) {
        var desk = deskRepositoryJpa.findById(id)
                .map(this::convertToDesk);

        if (desk.isEmpty())
            throw new NotImplemented();
        return desk;
    }

    @Override
    public Desk save(Desk entity) {
        DeskModel deskModel = convertToDeskModel(entity);
        deskModel = deskRepositoryJpa.save(deskModel);
        return convertToDesk(deskModel);
    }

    public Optional<T> findMappedObject(Long id) {
        var o = findById(id);
        if (o.isEmpty())
            return Optional.empty();
        Desk desk = o.get();
        Optional<T> t = map.get(desk);
        return t;
    }

    public void putObject(Long deskId, T t) {
        map.put(deskId, Optional.of(t));
    }

    public void removeObject(Long deskId) {
        map.put(deskId, Optional.empty());
    }

    public Optional<Map.Entry<Long, Optional<T>>> findMapping(Long deskId) {
        var desk = findById(deskId);
        if (desk.isEmpty()) return Optional.empty();
        return map.entrySet().stream().filter(deskOptionalEntry -> deskOptionalEntry.getKey().equals(deskId)).findFirst();
    }

    private Desk convertToDesk(DeskModel deskModel) {
        return Desk.of(deskModel.getId(), deskModel.getName(), deskModel.getMobile());
    }


    //    TODO: add mapstruct
    private DeskModel convertToDeskModel(Desk desk) {
        DeskModel deskModel = new DeskModel();
        deskModel.setId(desk.getId());
        deskModel.setName(desk.getName());
        deskModel.setMobile(desk.getMobile());
        return deskModel;
    }
}
