package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.collector.model.TemperatureSensorEvent;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TemperatureSensorEventHandler implements SensorEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorEvent sensorEvent = mapFromProtoToModel(event);
        eventProcessingService.processSensorEvent(sensorEvent);
    }

    private TemperatureSensorEvent mapFromProtoToModel(SensorEventProto sensorEventProto) {
        TemperatureSensorProto proto = sensorEventProto.getTemperatureSensorEvent();

        TemperatureSensorEvent event = new TemperatureSensorEvent();
        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        ));
        event.setTemperatureC(proto.getTemperatureC());
        event.setTemperatureF(proto.getTemperatureF());

        return event;
    }
}
