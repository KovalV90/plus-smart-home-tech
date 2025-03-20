package ru.yandex.practicum.analyzer.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;


@Slf4j
@Service
public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
        return switch (conditionType) {
            case LUMINOSITY -> lightSensor.getLuminosity();
            default -> null;
        };
    }
}