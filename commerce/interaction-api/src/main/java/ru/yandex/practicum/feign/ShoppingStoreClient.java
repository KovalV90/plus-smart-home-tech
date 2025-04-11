package ru.yandex.practicum.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreApi {
    @Override
    @PostMapping("/quantityState")
    void setProductQuantityState(@RequestBody SetProductQuantityStateRequest request);

    @Override
    @GetMapping
    List<ProductDto> getProductsByCategory(
            @RequestParam("category") ProductCategory category,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) List<String> sort);

    @Override
    @PutMapping
    ProductDto createNewProduct(@RequestBody NewProductRequest newProductRequest);

    @Override
    @PostMapping
    ProductDto updateProduct(@RequestBody UpdateProductRequest updateProductRequest);

    @Override
    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID id);

    @Override
    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable("productId") UUID productId);
}