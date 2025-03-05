package telemetry.collector.converter;

import telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
                throw new IllegalArgumentException("Unsupported sensor event type: " + event.getType());
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
                                        .setDeviceType(deviceAdded.getDeviceType().name())
                                        .build()
                        )
                        .build();
                break;
            // Дополнительные типы (DEVICE_REMOVED, SCENARIO_ADDED, SCENARIO_REMOVED) можно добавить аналогичным образом.
            default:
                throw new IllegalArgumentException("Unsupported hub event type: " + event.getType());
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
