package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryState;

@Component
public class DeliveryMapper {
    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .id(delivery.getId())
                .address(delivery.getAddress())
                .state(String.valueOf(delivery.getState()))
                .orderId(delivery.getOrderId())
                .build();
    }

    public Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                .id(dto.getId())
                .address(dto.getAddress())
                .state(DeliveryState.valueOf(dto.getState()))
                .orderId(dto.getOrderId())
                .build();
    }
}
