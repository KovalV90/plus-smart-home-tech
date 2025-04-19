package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    UUID productId;

    @NotBlank(message = "Название товара не может быть пустым")
    String productName;

    @NotBlank(message = "Описание товара не может быть пустым")
    String description;

    String imageSrc;

    @NotNull(message = "Статус остатка (quantityState) не может быть null")
    QuantityState quantityState;

    @NotNull(message = "Статус товара (productState) не может быть null")
    ProductState productState;

    ProductCategory productCategory;

    @DecimalMin(value = "1", message = "Цена товара не может быть меньше 1")
    Double price;
}