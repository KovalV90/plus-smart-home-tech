package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scenario_actions")
@Getter
@Setter
@NoArgsConstructor
public class ScenarioActionLink {

    @EmbeddedId
    private ScenarioActionLinkId id = new ScenarioActionLinkId();

    @ManyToOne
    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @ManyToOne
    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @ManyToOne
    @MapsId("actionId")
    @JoinColumn(name = "action_id", nullable = false)
    private ScenarioAction action;
}
