package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {

    @NotNull(message = "Идентификатор товара не может быть null")
    UUID productId;

    @NotNull(message = "Статус количества товара не может быть null")
    QuantityState quantityState;
}