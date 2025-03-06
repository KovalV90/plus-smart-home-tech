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


    public static byte[] convertSensorEvent(SensorEvent sensorEvent) throws IOException {
        Object payload = switch (sensorEvent) {
            case LightSensorEvent event -> LightSensorAvro.newBuilder()
                    .setLinkQuality(event.getLinkQuality())
                    .setLuminosity(event.getLuminosity())
                    .build();

            case SwitchSensorEvent event -> SwitchSensorAvro.newBuilder()
                    .setState(event.isState())
                    .build();

            case TemperatureSensorEvent event -> TemperatureSensorAvro.newBuilder()
                    .setTemperatureC(event.getTemperatureC())
                    .setTemperatureF(event.getTemperatureF())
                    .build();

            case ClimateSensorEvent event -> ClimateSensorAvro.newBuilder()
                    .setTemperatureC(event.getTemperatureC())
                    .setHumidity(event.getHumidity())
                    .setCo2Level(event.getCo2Level())
                    .build();

            case MotionSensorEvent event -> MotionSensorAvro.newBuilder()
                    .setLinkQuality(event.getLinkQuality())
                    .setMotion(event.isMotion())
                    .setVoltage(event.getVoltage())
                    .build();

            default -> throw new IllegalArgumentException("Неподдерживаемый тип события датчика: " + sensorEvent);
        };

        SensorEventAvro avroEvent = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp().toEpochMilli())
                .setPayload(payload)
                .build();


        return serializeAvro(avroEvent, SensorEventAvro.class);
    }

    public static byte[] convertHubEvent(HubEvent hubEvent) throws IOException {

        Object payload = switch (hubEvent) {
            case DeviceAddedEvent event -> DeviceAddedEventAvro.newBuilder()
                    .setId(event.getId())
                    .setType(event.getDeviceType())
                    .build();

            case DeviceRemovedEvent event -> DeviceRemovedEventAvro.newBuilder()
                    .setId(event.getId())
                    .build();

            case ScenarioAddedEvent event -> {
                List<ScenarioConditionAvro> avroConditions = event.getConditions().stream()
                        .map(cond -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(cond.getSensorId())
                                .setType(cond.getType())
                                .setOperation(cond.getOperation())
                                .setValue(cond.getValue())
                                .build())
                        .collect(Collectors.toList());

                List<DeviceActionAvro> avroActions = event.getActions().stream()
                        .map(action -> DeviceActionAvro.newBuilder()
                                .setSensorId(action.getSensorId())
                                .setType(action.getType())
                                .setValue(action.getValue())
                                .build())
                        .collect(Collectors.toList());

                yield ScenarioAddedEventAvro.newBuilder()
                        .setName(event.getName())
                        .setConditions(avroConditions)
                        .setActions(avroActions)
                        .build();
            }

            case ScenarioRemovedEvent event -> ScenarioRemovedEventAvro.newBuilder()
                    .setName(event.getName())
                    .build();

            default -> throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + hubEvent);
        };


        HubEventAvro avroEvent = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(payload)
                .build();

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
