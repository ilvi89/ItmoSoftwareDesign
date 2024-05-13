package ru.abolsoft.sseconnect.infr.repository.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import ru.abolsoft.sseconnect.core.entity.Status;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "badges")
@Builder
@Entity
public class BadgeModel implements Persistable<UUID> {
    @Id
    private UUID id;

    @Column(name = "custom_object_id")
    private Long customObjectId;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desk_id", referencedColumnName = "id")
    private DeskModel desk;

    @Override
    public boolean isNew() {
        return id == null;
    }
}