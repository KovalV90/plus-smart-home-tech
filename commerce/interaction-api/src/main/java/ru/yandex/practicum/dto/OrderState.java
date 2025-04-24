package ru.yandex.practicum.dto;

public enum OrderState {
    NEW,
    PAID,
    PAYMENT_FAILED,
    ASSEMBLED,
    ASSEMBLY_FAILED,
    DELIVERED,
    DELIVERY_FAILED,
    COMPLETED,
    PRODUCT_RETURNED,
    CANCELED,
    ON_PAYMENT,
    ON_DELIVERY,
    DONE
}
