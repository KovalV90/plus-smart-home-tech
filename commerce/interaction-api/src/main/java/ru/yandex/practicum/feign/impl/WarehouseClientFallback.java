package ru.yandex.practicum.feign.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.WarehouseClient;

@Slf4j
@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void newProductInWarehouse(AddNewProductInWarehouseRequest request) {
        log.warn("Сервис склада недоступен. Fallback: невозможно добавить новый товар. ID товара: {}", request.getProductId());
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        log.warn("Сервис склада недоступен. Fallback: невозможно пополнить остатки товара. ID товара: {}", request.getProductId());
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart) {
        log.warn("Сервис склада недоступен. Fallback: пропускаем проверку остатков для корзины: {}", cart.getShoppingCartId());

        BookedProductsDto fallbackResponse = new BookedProductsDto();
        fallbackResponse.setDeliveryWeight(0.0);
        fallbackResponse.setDeliveryVolume(0.0);
        fallbackResponse.setFragile(false);
        return fallbackResponse;
    }

    @Override
    public AddressDto getWarehouseAddress() {

        log.warn("Сервис склада недоступен. Fallback: возвращается пустой адрес");

        AddressDto address = new AddressDto();
        address.setCountry("Неизвестно");
        address.setCity("Недоступно");
        address.setStreet("-");
        address.setHouse("-");
        address.setFlat("-");
        return address;
    }
}