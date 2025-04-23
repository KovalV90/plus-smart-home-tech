package ru.yandex.practicum.feign.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feign.ShoppingCartApi;

import java.util.UUID;

@Slf4j
@Component
public class ShoppingCartClientFallback implements ShoppingCartApi {

    @Override
    public ShoppingCartDto getShoppingCartById(UUID id) {
        log.warn("Fallback getShoppingCartById: {}", id);
        return new ShoppingCartDto();
    }

    @Override
    public void deactivateShoppingCart(UUID id) {
        log.warn("Fallback deactivateShoppingCart: {}", id);
    }
}
