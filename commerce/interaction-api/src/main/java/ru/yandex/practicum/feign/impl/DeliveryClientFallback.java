package ru.yandex.practicum.feign.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feign.DeliveryClient;

import java.util.UUID;

@Slf4j
@Component
public class DeliveryClientFallback implements DeliveryClient {

    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        log.error("Fallback: createDelivery called. Delivery service unavailable.");
        return null;
    }

    @Override
    public DeliveryDto markDelivered(UUID id) {
        log.error("Fallback: markDelivered called. Delivery service unavailable.");
        return null;
    }

    @Override
    public DeliveryDto markFailed(UUID id) {
        log.error("Fallback: markFailed called. Delivery service unavailable.");
        return null;
    }


    @Override
    public Double calculateDeliveryCost(OrderDto orderDto) {
        return 0.0;
    }


    @Override
    public DeliveryDto markPicked(UUID orderId) {
        return null;
    }


}
