package ru.yandex.practicum.collector.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    @PositiveOrZero
    private int linkQuality;
    private boolean motion;
    @PositiveOrZero
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
