package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.collector.model.LightSensorEvent;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LightSensorEventHandler implements SensorEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        LightSensorEvent sensorEvent = mapFromProtoToModel(event);
        eventProcessingService.processSensorEvent(sensorEvent);
    }

    private LightSensorEvent mapFromProtoToModel(SensorEventProto sensorEventProto) {
        LightSensorProto proto = sensorEventProto.getLightSensorEvent();

        LightSensorEvent event = new LightSensorEvent();
        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        ));
        event.setLinkQuality(proto.getLinkQuality());
        event.setLuminosity(proto.getLuminosity());

        return event;
    }
}
