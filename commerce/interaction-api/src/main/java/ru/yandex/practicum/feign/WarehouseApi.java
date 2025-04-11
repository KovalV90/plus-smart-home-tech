package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

public interface WarehouseApi {


    void newProductInWarehouse(AddNewProductInWarehouseRequest request);


    void addProductToWarehouse(AddProductToWarehouseRequest request);


    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart);


    AddressDto getWarehouseAddress();
}
