package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feign.impl.ShoppingCartClientFallback;

import java.util.UUID;

@FeignClient(name = "shopping-cart", fallback = ShoppingCartClientFallback.class)
public interface ShoppingCartClient extends ShoppingCartApi {

    @Override
    @GetMapping("/api/v1/cart/{id}")
    ShoppingCartDto getShoppingCartById(@PathVariable("id") UUID id);

    @Override
    @PostMapping("/api/v1/cart/deactivate")
    void deactivateShoppingCart(@RequestBody UUID id);
}
