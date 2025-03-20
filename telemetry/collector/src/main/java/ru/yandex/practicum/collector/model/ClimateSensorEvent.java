package ru.yandex.practicum.collector.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.avro.reflect.AvroName;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    @AvroName("temperature_c")
    private int temperatureC;
    @PositiveOrZero
    @AvroName("humidity")
    private int humidity;
    @PositiveOrZero
    @AvroName("co2_level")
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
