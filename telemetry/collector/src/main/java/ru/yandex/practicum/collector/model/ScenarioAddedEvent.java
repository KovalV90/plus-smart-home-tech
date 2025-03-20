package ru.yandex.practicum.collector.model;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;

    @NotNull
    private List<ScenarioCondition> conditions;

    @NotNull
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
