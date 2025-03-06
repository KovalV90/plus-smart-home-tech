package ru.yandex.practicum.collector.converter;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import ru.yandex.practicum.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AvroConverter {

    public static byte[] convertSensorEvent(SensorEvent event) throws IOException {
        SensorEventAvro avroEvent;
        switch (event.getType()) {
            case LIGHT_SENSOR_EVENT:
                LightSensorEvent lightEvent = (LightSensorEvent) event;
                avroEvent = SensorEventAvro.newBuilder()
                        .setId(lightEvent.getId())
                        .setHubId(lightEvent.getHubId())
                        .setTimestamp(lightEvent.getTimestamp().toEpochMilli())
                        .setPayload(
                                LightSensorAvro.newBuilder()
                                        .setLinkQuality(lightEvent.getLinkQuality())
                                        .setLuminosity(lightEvent.getLuminosity())
                                        .build()
                        )
                        .build();
                break;
            case SWITCH_SENSOR_EVENT:
                SwitchSensorEvent switchEvent = (SwitchSensorEvent) event;
                avroEvent = SensorEventAvro.newBuilder()
                        .setId(switchEvent.getId())
                        .setHubId(switchEvent.getHubId())
                        .setTimestamp(switchEvent.getTimestamp().toEpochMilli())
                        .setPayload(
                                SwitchSensorAvro.newBuilder()
                                        .setState(switchEvent.isState())
                                        .build()
                        )
                        .build();
                break;
            case TEMPERATURE_SENSOR_EVENT:
                TemperatureSensorEvent tempEvent = (TemperatureSensorEvent) event;
                avroEvent = SensorEventAvro.newBuilder()
                        .setId(tempEvent.getId())
                        .setHubId(tempEvent.getHubId())
                        .setTimestamp(tempEvent.getTimestamp().toEpochMilli())
                        .setPayload(
                                TemperatureSensorAvro.newBuilder()
                                        .setTemperatureC(tempEvent.getTemperatureC())
                                        .setTemperatureF(tempEvent.getTemperatureF())
                                        .build()
                        )
                        .build();
                break;
            case CLIMATE_SENSOR_EVENT:
                ClimateSensorEvent climateEvent = (ClimateSensorEvent) event;
                avroEvent = SensorEventAvro.newBuilder()
                        .setId(climateEvent.getId())
                        .setHubId(climateEvent.getHubId())
                        .setTimestamp(climateEvent.getTimestamp().toEpochMilli())
                        .setPayload(
                                ClimateSensorAvro.newBuilder()
                                        .setTemperatureC(climateEvent.getTemperatureC())
                                        .setHumidity(climateEvent.getHumidity())
                                        .setCo2Level(climateEvent.getCo2Level())
                                        .build()
                        )
                        .build();
                break;
            case MOTION_SENSOR_EVENT:
                MotionSensorEvent motionEvent = (MotionSensorEvent) event;
                avroEvent = SensorEventAvro.newBuilder()
                        .setId(motionEvent.getId())
                        .setHubId(motionEvent.getHubId())
                        .setTimestamp(motionEvent.getTimestamp().toEpochMilli())
                        .setPayload(
                                MotionSensorAvro.newBuilder()
                                        .setLinkQuality(motionEvent.getLinkQuality())
                                        .setMotion(motionEvent.isMotion())
                                        .setVoltage(motionEvent.getVoltage())
                                        .build()
                        )
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип события датчика: " + event.getType());
        }
        return serializeAvro(avroEvent, SensorEventAvro.class);
    }

    public static byte[] convertHubEvent(HubEvent event) throws IOException {
        HubEventAvro avroEvent;
        switch (event.getType()) {
            case DEVICE_ADDED:
                DeviceAddedEvent deviceAdded = (DeviceAddedEvent) event;
                avroEvent = HubEventAvro.newBuilder()
                        .setHubId(deviceAdded.getHubId())
                        .setTimestamp(deviceAdded.getTimestamp().toEpochMilli())
                        .setPayload(
                                DeviceAddedEventAvro.newBuilder()
                                        .setId(deviceAdded.getId())
                                        .setType(deviceAdded.getDeviceType())
                                        .build()
                        )
                        .build();
                break;
            case DEVICE_REMOVED:
                DeviceRemovedEvent deviceRemoved = (DeviceRemovedEvent) event;
                avroEvent = HubEventAvro.newBuilder()
                        .setHubId(deviceRemoved.getHubId())
                        .setTimestamp(deviceRemoved.getTimestamp().toEpochMilli())
                        .setPayload(
                                DeviceRemovedEventAvro.newBuilder()
                                        .setId(deviceRemoved.getId())
                                        .build()
                        )
                        .build();
                break;
            case SCENARIO_ADDED:
                ScenarioAddedEvent scenarioAdded = (ScenarioAddedEvent) event;
                List<ScenarioConditionAvro> avroConditions = scenarioAdded.getConditions().stream()
                        .map(cond -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(cond.getSensorId())
                                .setType(cond.getType())
                                .setOperation(cond.getOperation())
                                .setValue(cond.getValue())
                                .build())
                        .collect(Collectors.toList());
                List<DeviceActionAvro> avroActions = scenarioAdded.getActions().stream()
                        .map(action -> DeviceActionAvro.newBuilder()
                                .setSensorId(action.getSensorId())
                                .setType(action.getType())
                                .setValue(action.getValue())
                                .build())
                        .collect(Collectors.toList());
                avroEvent = HubEventAvro.newBuilder()
                        .setHubId(scenarioAdded.getHubId())
                        .setTimestamp(scenarioAdded.getTimestamp().toEpochMilli())
                        .setPayload(
                                ScenarioAddedEventAvro.newBuilder()
                                        .setName(scenarioAdded.getName())
                                        .setConditions(avroConditions)
                                        .setActions(avroActions)
                                        .build()
                        )
                        .build();
                break;
            case SCENARIO_REMOVED:
                ScenarioRemovedEvent scenarioRemoved = (ScenarioRemovedEvent) event;
                avroEvent = HubEventAvro.newBuilder()
                        .setHubId(scenarioRemoved.getHubId())
                        .setTimestamp(scenarioRemoved.getTimestamp().toEpochMilli())
                        .setPayload(
                                ScenarioRemovedEventAvro.newBuilder()
                                        .setName(scenarioRemoved.getName())
                                        .build()
                        )
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + event.getType());
        }
        return serializeAvro(avroEvent, HubEventAvro.class);
    }

    private static <T> byte[] serializeAvro(T avroObject, Class<T> clazz) throws IOException {
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(clazz);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(avroObject, encoder);
        encoder.flush();
        return out.toByteArray();
    }
}
