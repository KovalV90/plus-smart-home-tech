package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.collector.model.MotionSensorEvent;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class MotionSensorEventHandler implements SensorEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        MotionSensorEvent sensorEvent = mapFromProtoToModel(event);
        eventProcessingService.processSensorEvent(sensorEvent);
    }

    private MotionSensorEvent mapFromProtoToModel(SensorEventProto sensorEventProto) {
        MotionSensorProto proto = sensorEventProto.getMotionSensorEvent();

        MotionSensorEvent event = new MotionSensorEvent();
        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        ));
        event.setMotion(proto.getMotion());
        event.setLinkQuality(proto.getLinkQuality());
        event.setVoltage(proto.getVoltage());

        return event;
    }
}
