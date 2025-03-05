package telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    @NotBlank
    private String sensorId; // Идентификатор датчика

    @NotNull
    private ConditionType type; // Например, MOTION, LUMINOSITY, SWITCH, TEMPERATURE, CO2LEVEL, HUMIDITY

    @NotNull
    private ConditionOperation operation; // EQUALS, GREATER_THAN, LOWER_THAN

    // Значение условия может быть числовым, логическим или отсутствовать (null)
    private Object value;
}
