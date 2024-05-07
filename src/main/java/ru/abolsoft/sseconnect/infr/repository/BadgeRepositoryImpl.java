package ru.abolsoft.sseconnect.infr.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.entity.Status;
import ru.abolsoft.sseconnect.core.repository.BadgeRepository;
import ru.abolsoft.sseconnect.infr.repository.model.BadgeModel;
import ru.abolsoft.sseconnect.infr.repository.model.DeskModel;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BadgeRepositoryImpl implements BadgeRepository {
    private final BadgeRepositoryJpa badgeRepositoryJpa;


    @Override
    public Optional<Status> findStatusById(Long badgeId) {
        return badgeRepositoryJpa.findStatusById(badgeId);
    }

    @Override
    public Optional<Badge> findById(Long id) {
        return badgeRepositoryJpa.findById(id).map(this::convertToBadge);
    }

    @Override
    public Badge save(Badge entity) {
        BadgeModel model = convertToBadgeModel(entity);
        model = badgeRepositoryJpa.save(model);
        return convertToBadge(model);
    }

    //    TODO: add mapstruct
    private Badge convertToBadge(BadgeModel model) {
        return Badge.of(model.getId(), model.getMemberId(), convertToDesk(model.getDesk()), model.getStatus());
    }


    private BadgeModel convertToBadgeModel(Badge badge) {
        var m = new BadgeModel();
        m.setId(badge.getId());
        m.setMemberId(badge.getOwnerId());
        m.setStatus(badge.getStatus());
        m.setDesk(convertToDeskModel(badge.getDesk()));
        return m;
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
