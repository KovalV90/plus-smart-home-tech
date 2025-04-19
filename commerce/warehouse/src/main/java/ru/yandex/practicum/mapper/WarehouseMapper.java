package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.AddNewProductInWarehouseRequest;
import ru.yandex.practicum.model.WarehouseProduct;

public class WarehouseMapper {

    public static WarehouseProduct toWarehouseProduct(AddNewProductInWarehouseRequest request) {
        return WarehouseProduct.builder()
                .productId(request.getProductId())
                .fragile(request.isFragile())
                .weight(request.getWeight())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .width(request.getDimension().getWidth())
                .quantity(0L)
                .build();
    }
}