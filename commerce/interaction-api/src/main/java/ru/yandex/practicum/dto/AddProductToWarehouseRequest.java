package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
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
public class AddProductToWarehouseRequest {

    @NotNull(message = "Идентификатор продукта не может быть null")
    UUID productId;

    @NotNull(message = "Количество добавляемого товара обязательно")
    @Min(value = 1, message = "Количество добавляемого товара не может быть меньше 1")
    Long quantity;
}
