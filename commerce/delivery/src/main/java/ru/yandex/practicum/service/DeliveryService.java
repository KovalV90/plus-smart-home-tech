package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto dto);
    DeliveryDto markDelivered(UUID id);
    DeliveryDto markFailed(UUID id);
    DeliveryDto markInProgress(UUID id);
    DeliveryDto cancelDelivery(UUID id);
    Double calculateDeliveryCost(UUID orderId);
}
