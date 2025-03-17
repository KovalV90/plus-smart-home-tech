package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.collector.model.ScenarioRemovedEvent;
import ru.yandex.practicum.collector.model.HubEventType;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioRemovedEvent scenarioRemovedEvent = mapFromProtoToModel(event);
        eventProcessingService.processHubEvent(scenarioRemovedEvent);
    }

    private ScenarioRemovedEvent mapFromProtoToModel(HubEventProto hubEventProto) {
        ScenarioRemovedEventProto proto = hubEventProto.getScenarioRemoved();

        ScenarioRemovedEvent event = new ScenarioRemovedEvent();
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds(),
                hubEventProto.getTimestamp().getNanos()
        ));
        event.setType(HubEventType.SCENARIO_REMOVED);
        event.setName(proto.getName());

        return event;
    }
}
