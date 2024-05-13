package ru.abolsoft.sseconnect.infr.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "badge_preset")
public class BadgePresetModel {
    @Id
    private UUID id;

    @Column(name = "alias")
    private String alias;

    @OneToMany(mappedBy = "badgePreset", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<BadgePresetPropertyModel> properties;
}
