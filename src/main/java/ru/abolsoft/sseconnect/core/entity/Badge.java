package ru.abolsoft.sseconnect.core.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.abolsoft.sseconnect.base.entity.BaseAggregate;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;

import java.util.UUID;

@Getter
@Setter
public class Badge extends BaseAggregate<UUID> {
    private Long customObjectId;
    private Long ownerId;
    private Desk desk;
    private Status status;


    public static Badge create(UUID id, Long customObjectId, Long ownerId) {
        var b = new Badge();
        b.setId(id);
        b.setCustomObjectId(customObjectId);
        b.setOwnerId(ownerId);
        b.setStatus(Status.NEW);
        b.setDesk(null); //TODO: default object (static obgect Desk.Default) with check
        return b;
    }

    public static Badge of(UUID id, Long customObjectId, Long ownerId, Desk desk, Status status) {
        var b = Badge.create(id, customObjectId, ownerId);
        b.setDesk(desk);
        b.setStatus(status);
        return b;
    }


    public void startProcessing(Desk desk) {
        if (!getStatus().equals(Status.NEW))
            throw new NotImplemented();
        if (desk != null)
            setDesk(desk);
        this.setDesk(desk);
        this.chaneStatus(Status.IN_PROCESS);
    }

    public void chaneStatus(Status status) {
        // TODO: add some checks
        this.status = status;
    }

    public void stopProcessing() {
        var inProcess = getStatus().equals(Status.IN_PROCESS);
        var isAccepted = getStatus().equals(Status.ACCEPTED);
        if (!inProcess)
            if (!isAccepted)
                throw new NotImplemented();
        if (desk == null)
            throw new NotImplemented();
        this.setStatus(Status.ACCEPTED);

    }
}



