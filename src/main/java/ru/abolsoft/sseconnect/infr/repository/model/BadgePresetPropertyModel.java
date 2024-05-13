package ru.abolsoft.sseconnect.infr.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "badge_preset_properties")
public class BadgePresetPropertyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_preset_id")
    private BadgePresetModel badgePreset;

    @Column(name = "property_key")
    private String key;

    @Column(name = "property_value")
    private String value;

    // Constructors, getters, and setters
}
