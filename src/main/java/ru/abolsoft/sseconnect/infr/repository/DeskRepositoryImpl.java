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
    private final Map<Desk, Optional<T>> map = new HashMap<>();


    @Override
    public Optional<Desk> findById(Long id) {
        var desk = deskRepositoryJpa.findById(id)
                .map(this::convertToDesk);

        if (desk.isEmpty())
            throw new NotImplemented();
        map.putIfAbsent(desk.get(), Optional.empty());
        return desk;
    }

    @Override
    public Desk save(Desk entity) {
        DeskModel deskModel = convertToDeskModel(entity);
        deskModel = deskRepositoryJpa.save(deskModel);

        var saved = this.findMapping(deskModel.getId());
        if (saved.isPresent() && saved.get().getKey().equals(entity)) {
            Optional<T> obj = saved.get().getValue();
            map.remove(entity);
            map.put(entity, obj);
            return entity;
        }
        entity.setId(deskModel.getId());
        map.put(entity, Optional.empty());
        return entity;
    }

    public Optional<T> findMappedObject(Long id) {
        var o = findDesk(id);
        if (o.isEmpty())
            return Optional.empty();
        Desk desk = o.get();
        Optional<T> t = map.get(desk);
        return t;
    }

    private Optional<Desk> findDesk(Long id) {
        return findById(id);
    }

    public void put(Long deskId, T t) {
        var o = findDesk(deskId);
        if (o.isEmpty())
            throw new NotImplemented();
        map.put(o.get(), Optional.of(t));
    }

    public void removeObject(Long deskId) {
        log.warn("removeObject");
        var o = findDesk(deskId);
        if (o.isEmpty())
            throw new NotImplemented();
        map.put(o.get(), Optional.empty());
    }

    public Optional<Map.Entry<Desk, Optional<T>>> findMapping(Long deskId) {
        var desk = findById(deskId);
        if (desk.isEmpty()) return Optional.empty();
        return map.entrySet().stream().filter(deskOptionalEntry -> deskOptionalEntry.getKey().getId().equals(deskId)).findFirst();
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
