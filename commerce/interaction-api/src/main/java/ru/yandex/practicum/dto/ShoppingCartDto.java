package ru.yandex.practicum.dto;

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
public class ShoppingCartDto {

    UUID shoppingCartId;

    @NotNull(message = "Список товаров не может быть null")
    Map<UUID, Long> products;
}