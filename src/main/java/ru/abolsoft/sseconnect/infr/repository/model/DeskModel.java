package ru.abolsoft.sseconnect.infr.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "desks")
@Builder
@Entity
public class DeskModel {
    @Id
    @GeneratedValue(generator = "desks_id_seq")
    @SequenceGenerator(name = "desks_id_seq", sequenceName = "desks_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_connection")
    private Boolean mobile;
}