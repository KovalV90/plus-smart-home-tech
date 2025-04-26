package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.UUID;

public interface ShoppingCartApi {
    ShoppingCartDto getShoppingCartById(UUID id);
    void deactivateShoppingCart(UUID id);
}
