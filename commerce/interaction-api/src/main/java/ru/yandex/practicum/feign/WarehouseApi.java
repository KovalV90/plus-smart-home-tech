package ru.yandex.practicum.feign;


import ru.yandex.practicum.dto.*;

import java.util.UUID;

public interface WarehouseApi {


    void newProductInWarehouse(AddNewProductInWarehouseRequest request);


    void addProductToWarehouse(AddProductToWarehouseRequest request);


    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart);


    AddressDto getWarehouseAddress();

    void assemblyProductForOrderFromShoppingCart(ShoppingCartDto cart);

    void returnProducts(BookedProductsDto bookedProducts);

    void shippedToDelivery(UUID orderId, UUID deliveryId);
}
