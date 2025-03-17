package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.collector.model.DeviceAddedEvent;  // Используем модель события
import ru.yandex.practicum.collector.model.DeviceType;
import ru.yandex.practicum.collector.model.HubEventType;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceAddedEvent deviceAddedEvent = mapFromProtoToModel(event);
        eventProcessingService.processHubEvent(deviceAddedEvent);
    }

    private DeviceAddedEvent mapFromProtoToModel(HubEventProto hubEventProto) {
        DeviceAddedEventProto proto = hubEventProto.getDeviceAdded();
        DeviceAddedEvent event = new DeviceAddedEvent();
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds(),
                hubEventProto.getTimestamp().getNanos()
        ));
        event.setType(HubEventType.DEVICE_ADDED);
        event.setId(proto.getId());
        event.setDeviceType(DeviceType.valueOf(proto.getType().name()));

        return event;
    }
}
