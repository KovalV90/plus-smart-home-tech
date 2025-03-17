package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.collector.model.DeviceRemovedEvent;
import ru.yandex.practicum.collector.model.HubEventType;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceRemovedEvent deviceRemovedEvent = mapFromProtoToModel(event);
        eventProcessingService.processHubEvent(deviceRemovedEvent);
    }

    private DeviceRemovedEvent mapFromProtoToModel(HubEventProto hubEventProto) {
        DeviceRemovedEventProto proto = hubEventProto.getDeviceRemoved();
        DeviceRemovedEvent event = new DeviceRemovedEvent();
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds(),
                hubEventProto.getTimestamp().getNanos()
        ));
        event.setType(HubEventType.DEVICE_REMOVED);
        event.setId(proto.getId());

        return event;
    }
}
