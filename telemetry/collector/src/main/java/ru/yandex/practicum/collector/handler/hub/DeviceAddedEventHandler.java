package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final EventProcessingService eventService;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        eventService.sendHubEvent(mapFromProtoToAvro(event));
    }

    private HubEventAvro mapFromProtoToAvro(HubEventProto hubEventProto) {
        DeviceAddedEventProto deviceAddedEventProto = hubEventProto.getDeviceAdded();
        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEventProto.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEventProto.getType().name()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds(), hubEventProto.getTimestamp().getNanos()))
                .setPayload(deviceAddedEventAvro)
                .build();
    }

}
