package ru.yandex.practicum.feign;


import ru.yandex.practicum.dto.*;

public interface WarehouseApi {


    void newProductInWarehouse(AddNewProductInWarehouseRequest request);


    void addProductToWarehouse(AddProductToWarehouseRequest request);


    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart);


    AddressDto getWarehouseAddress();
}
