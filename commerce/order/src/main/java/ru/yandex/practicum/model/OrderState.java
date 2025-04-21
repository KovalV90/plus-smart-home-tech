package ru.yandex.practicum.model;

public enum OrderState {
    NEW,
    PAID,
    PAYMENT_FAILED,
    ASSEMBLED,
    ASSEMBLY_FAILED,
    DELIVERED,
    DELIVERY_FAILED,
    COMPLETED,
    PRODUCT_RETURNED
}
