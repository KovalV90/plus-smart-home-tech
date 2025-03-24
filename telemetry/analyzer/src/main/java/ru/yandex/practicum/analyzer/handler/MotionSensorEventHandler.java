package ru.yandex.practicum.analyzer.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;


@Slf4j
@Service
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return MotionSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        MotionSensorAvro motionSensor = (MotionSensorAvro) sensorState.getData();
        return switch (conditionType) {
            case MOTION -> motionSensor.getMotion() ? 1 : 0;
            default -> null;
        };
    }
}