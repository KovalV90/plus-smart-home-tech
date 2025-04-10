package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

@RestController
@RequestMapping("/api/v1/warehouse")
public interface WarehouseApi {

    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid AddNewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto cart);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
