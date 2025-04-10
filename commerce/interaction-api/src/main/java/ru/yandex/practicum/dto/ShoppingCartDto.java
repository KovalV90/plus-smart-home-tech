package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartDto {

    UUID shoppingCartId;

    @NotNull(message = "Список товаров не может быть null")
    Map<UUID, Long> products;
}