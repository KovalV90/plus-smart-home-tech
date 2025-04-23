package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Вес доставки не может быть null")
    @DecimalMin(value = "0.0", message = "Вес доставки не может быть отрицательным")
    Double deliveryWeight;

    @NotNull(message = "Объём доставки не может быть null")
    @DecimalMin(value = "0.0", message = "Объём доставки не может быть отрицательным")
    Double deliveryVolume;

    boolean fragile;
    Map<UUID, Long> products;

}