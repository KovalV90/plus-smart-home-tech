package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.collector.service.EventProcessingService;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.collector.model.ClimateSensorEvent;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ClimateSensorEventHandler implements SensorEventHandler {

    private final EventProcessingService eventProcessingService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        ClimateSensorEvent sensorEvent = mapFromProtoToModel(event);
        eventProcessingService.processSensorEvent(sensorEvent);
    }

    private ClimateSensorEvent mapFromProtoToModel(SensorEventProto sensorEventProto) {
        ClimateSensorProto proto = sensorEventProto.getClimateSensorEvent();

        ClimateSensorEvent event = new ClimateSensorEvent();
        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        ));
        event.setTemperatureC(proto.getTemperatureC());
        event.setHumidity(proto.getHumidity());
        event.setCo2Level(proto.getCo2Level());

        return event;
    }
}
