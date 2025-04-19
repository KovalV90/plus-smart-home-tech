package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AddNewProductInWarehouseRequest {

    @NotNull(message = "Идентификатор продукта не может быть null")
    UUID productId;

    boolean fragile;

    @NotNull(message = "Габариты продукта не могут быть null")
    DimensionDto dimension;

    @NotNull(message = "Вес продукта не может быть null")
    @DecimalMin(value = "1", message = "Вес товара не может быть меньше 1")
    Double weight;
}
