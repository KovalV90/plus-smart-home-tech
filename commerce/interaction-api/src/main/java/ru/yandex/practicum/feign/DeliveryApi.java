package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryApi {

    DeliveryDto createDelivery(DeliveryDto dto);

    DeliveryDto markDelivered(UUID id);

    DeliveryDto markFailed(UUID id);
    Double calculateDeliveryCost(OrderDto orderId);
    DeliveryDto markPicked(UUID orderId);
}
