package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface OrderApi {

    OrderDto markAsPaid(UUID orderId);

    OrderDto markAsPaymentFailed(UUID orderId);

    OrderDto markAsDelivered(UUID orderId);

    OrderDto markAsDeliveryFailed(UUID orderId);

    OrderDto completeOrder(UUID orderId);
}
