package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Id
    private String id;
    @Column(name = "hub_id")
    private String hubId;
}
