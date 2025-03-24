package ru.yandex.practicum.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class ScenarioCondition {
    @NotBlank
    private String sensorId;

    @NotNull
    private ConditionType type;

    @NotNull
    private OperationType operation;

    int value;
}
