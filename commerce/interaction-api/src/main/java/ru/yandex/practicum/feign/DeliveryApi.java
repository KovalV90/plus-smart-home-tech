package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.DeliveryDto;

import java.util.UUID;

public interface DeliveryApi {

    DeliveryDto createDelivery(DeliveryDto dto);

    DeliveryDto markDelivered(UUID id);

    DeliveryDto markFailed(UUID id);
}
