package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.collector.model.SwitchSensorEvent;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SwitchSensorEventHandler implements SensorEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        SwitchSensorEvent sensorEvent = mapFromProtoToModel(event);
        eventProcessingService.processSensorEvent(sensorEvent);
    }

    private SwitchSensorEvent mapFromProtoToModel(SensorEventProto sensorEventProto) {
        SwitchSensorProto proto = sensorEventProto.getSwitchSensorEvent();

        SwitchSensorEvent event = new SwitchSensorEvent();
        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        ));
        event.setState(proto.getState());

        return event;
    }
}
