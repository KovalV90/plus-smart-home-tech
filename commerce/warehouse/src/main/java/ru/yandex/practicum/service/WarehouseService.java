package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.UUID;

public interface WarehouseService {
    void newProductInWarehouse(AddNewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    void assemblyProductForOrderFromShoppingCart(ShoppingCartDto cart);

    void returnProducts(BookedProductsDto bookedProducts);

    void shippedToDelivery(UUID orderId, UUID deliveryId);

}
