package ru.abolsoft.sseconnect.infr.repository.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import ru.abolsoft.sseconnect.core.entity.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "badges")
@Builder
@Entity
public class BadgeModel implements Persistable<Long> {
    @Id
    private Long id;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "desk_id", referencedColumnName = "id")
    private DeskModel desk;

    @Override
    public boolean isNew() {
        return id == null;
    }
}