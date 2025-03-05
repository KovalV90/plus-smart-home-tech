package telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DeviceAction {
    @NotBlank
    private String sensorId; // Идентификатор датчика

    @NotNull
    private ActionType type; // ACTIVATE, DEACTIVATE, INVERSE, SET_VALUE

    // Значение действия, если применимо; может быть null
    private Integer value;
}
