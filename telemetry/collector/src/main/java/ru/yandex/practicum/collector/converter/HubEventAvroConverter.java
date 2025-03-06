package ru.yandex.practicum.collector.converter;

import ru.yandex.practicum.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

public class HubEventAvroConverter {

    public static HubEventAvro convert(HubEvent hubEvent) {
        Object payload;
        switch (hubEvent) {
            case DeviceAddedEvent event -> {
                payload = DeviceAddedEventAvro.newBuilder()
                        .setId(event.getId())
                        .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                        .build();
            }
            case DeviceRemovedEvent event -> {
                payload = DeviceRemovedEventAvro.newBuilder()
                        .setId(event.getId())
                        .build();
            }
            case ScenarioAddedEvent event -> {
                List<ScenarioConditionAvro> avroConditions = event.getConditions().stream()
                        .map(cond -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(cond.getSensorId())
                                .setType(ConditionTypeAvro.valueOf(cond.getType().name()))
                                .setOperation(ConditionOperationAvro.valueOf(cond.getOperation().name()))
                                .setValue(cond.getValue())
                                .build())
                        .collect(Collectors.toList());

                List<DeviceActionAvro> avroActions = event.getActions().stream()
                        .map(action -> DeviceActionAvro.newBuilder()
                                .setSensorId(action.getSensorId())
                                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                                .setValue(action.getValue())
                                .build())
                        .collect(Collectors.toList());

                payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(event.getName())
                        .setConditions(avroConditions)
                        .setActions(avroActions)
                        .build();
            }
            case ScenarioRemovedEvent event -> {
                payload = ScenarioRemovedEventAvro.newBuilder()
                        .setName(event.getName())
                        .build();
            }
            default -> throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + hubEvent);
        }

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
