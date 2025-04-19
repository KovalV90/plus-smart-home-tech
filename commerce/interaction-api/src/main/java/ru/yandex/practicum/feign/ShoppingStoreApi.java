package ru.yandex.practicum.feign;

import feign.FeignException;

import ru.yandex.practicum.dto.*;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreApi {

    void setProductQuantityState(SetProductQuantityStateRequest request) throws FeignException;

    List<ProductDto> getProductsByCategory(
            ProductCategory category,
            int page,
            int size,
            List<String> sort);

    ProductDto createNewProduct(NewProductRequest newProductRequest);

    ProductDto updateProduct(UpdateProductRequest updateProductRequest);

    Boolean removeProductFromStore(UUID id);

    ProductDto getProductById(UUID productId);
}
