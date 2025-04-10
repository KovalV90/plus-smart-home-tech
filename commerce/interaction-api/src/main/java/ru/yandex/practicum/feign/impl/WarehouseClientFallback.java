package ru.yandex.practicum.feign.impl;


import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feign.WarehouseClient;

public class WarehouseClientFallback implements WarehouseClient {
    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart) {
        BookedProductsDto fallbackResponse = new BookedProductsDto();
        fallbackResponse.setDeliveryWeight(0.0);
        fallbackResponse.setDeliveryVolume(0.0);
        fallbackResponse.setFragile(false);
        return fallbackResponse;
    }
}