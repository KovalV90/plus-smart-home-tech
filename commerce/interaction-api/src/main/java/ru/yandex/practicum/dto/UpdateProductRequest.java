package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @NotNull(message = "Идентификатор продукта обязателен")
    UUID productId;

    @NotBlank(message = "Название продукта не может быть пустым")
    String productName;

    @NotBlank(message = "Описание продукта не может быть пустым")
    String description;

    String imageSrc;

    @NotNull(message = "Состояние остатка обязательно")
    QuantityState quantityState;

    @NotNull(message = "Статус продукта обязателен")
    ProductState productState;

    ProductCategory productCategory;

    @Min(value = 1, message = "Цена не может быть меньше 1")
    Double price;
}