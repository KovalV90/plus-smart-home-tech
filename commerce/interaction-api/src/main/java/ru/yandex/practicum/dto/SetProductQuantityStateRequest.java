package ru.yandex.practicum.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class SetProductQuantityStateRequest {

    @NotNull(message = "Идентификатор товара не может быть null")
    UUID productId;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус количества товара не может быть null")
    QuantityState quantityState;
}