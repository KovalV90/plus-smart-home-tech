package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.collector.model.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEvent scenarioAddedEvent = mapFromProtoToModel(event);
        eventProcessingService.processHubEvent(scenarioAddedEvent);
    }

    private ScenarioAddedEvent mapFromProtoToModel(HubEventProto hubEventProto) {
        ScenarioAddedEventProto proto = hubEventProto.getScenarioAdded();

        ScenarioAddedEvent event = new ScenarioAddedEvent();
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds(),
                hubEventProto.getTimestamp().getNanos()
        ));
        event.setType(HubEventType.SCENARIO_ADDED);
        event.setName(proto.getName());
        event.setConditions(mapConditions(proto.getConditionList()));
        event.setActions(mapActions(proto.getActionList()));

        return event;
    }

    private static List<ScenarioCondition> mapConditions(List<ScenarioConditionProto> protoConditions) {
        return protoConditions.stream().map(proto -> {
            Object value = switch (proto.getValueCase()) {
                case INT_VALUE -> proto.getIntValue();
                case BOOL_VALUE -> proto.getBoolValue();
                default -> null;
            };

            ScenarioCondition condition = new ScenarioCondition();
            condition.setSensorId(proto.getSensorId());
            condition.setType(ConditionType.valueOf(proto.getType().name()));
            condition.setOperation(ConditionOperation.valueOf(proto.getOperation().name()));
            condition.setValue(value);

            return condition;
        }).collect(Collectors.toList());
    }

    private static List<DeviceAction> mapActions(List<DeviceActionProto> protoActions) {
        return protoActions.stream().map(proto -> {
            DeviceAction action = new DeviceAction();
            action.setSensorId(proto.getSensorId());
            action.setType(ActionType.valueOf(proto.getType().name()));
            action.setValue(proto.hasValue() ? proto.getValue() : null);

            return action;
        }).collect(Collectors.toList());
    }
}
