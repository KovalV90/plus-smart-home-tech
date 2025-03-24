package ru.yandex.practicum.analyzer.handler;

import ru.yandex.practicum.analyzer.model.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;


public interface SensorEventHandler {

    String getSensorType();

    Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState);

}