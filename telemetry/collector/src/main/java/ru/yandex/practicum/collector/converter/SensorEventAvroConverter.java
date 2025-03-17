package ru.yandex.practicum.collector.converter;

import ru.yandex.practicum.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

public class SensorEventAvroConverter {

    public static SensorEventAvro convert(SensorEvent sensorEvent) {
        Object payload;
        switch (sensorEvent) {
            case LightSensorEvent event -> {
                payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(event.getLinkQuality())
                        .setLuminosity(event.getLuminosity())
                        .build();
            }
            case SwitchSensorEvent event -> {
                payload = SwitchSensorAvro.newBuilder()
                        .setState(event.isState())
                        .build();
            }
            case TemperatureSensorEvent event -> {
                payload = TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(event.getTemperatureC())
                        .setTemperatureF(event.getTemperatureF())
                        .build();
            }
            case ClimateSensorEvent event -> {
                payload = ClimateSensorAvro.newBuilder()
                        .setTemperatureC(event.getTemperatureC())
                        .setHumidity(event.getHumidity())
                        .setCo2Level(event.getCo2Level())
                        .build();
            }
            case MotionSensorEvent event -> {
                payload = MotionSensorAvro.newBuilder()
                        .setLinkQuality(event.getLinkQuality())
                        .setMotion(event.isMotion())
                        .setVoltage(event.getVoltage())
                        .build();
            }
            default -> throw new IllegalArgumentException("Неподдерживаемый тип события датчика: " + sensorEvent);
        }

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                //.setTimestamp(sensorEvent.getTimestamp())

                .setTimestamp(Instant.ofEpochMilli(sensorEvent.getTimestamp().toEpochMilli()))
                .setPayload(payload)
                .build();
    }
}
