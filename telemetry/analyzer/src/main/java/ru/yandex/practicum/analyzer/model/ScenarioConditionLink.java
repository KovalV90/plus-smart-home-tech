package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scenario_conditions")
@Getter
@Setter
@NoArgsConstructor
public class ScenarioConditionLink {


    @EmbeddedId
    private ScenarioConditionLinkId id = new ScenarioConditionLinkId();

    @ManyToOne
    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @ManyToOne
    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @ManyToOne
    @MapsId("conditionId")
    @JoinColumn(name = "condition_id", nullable = false)
    private ScenarioCondition condition;
}
