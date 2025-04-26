package ru.yandex.practicum.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @PostMapping("/quantityState")
    void setProductQuantityState(@RequestBody SetProductQuantityStateRequest request);


    @GetMapping
    List<ProductDto> getProductsByCategory(
            @RequestParam("category") ProductCategory category,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) List<String> sort);


    @PutMapping
    ProductDto createNewProduct(@RequestBody NewProductRequest newProductRequest);


    @PostMapping
    ProductDto updateProduct(@RequestBody UpdateProductRequest updateProductRequest);


    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID id);


    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable("productId") UUID productId);
}