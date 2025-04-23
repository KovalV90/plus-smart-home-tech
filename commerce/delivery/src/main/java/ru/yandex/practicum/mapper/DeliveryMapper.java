package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryState;

@Component
public class DeliveryMapper {
    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .id(delivery.getId())
                .address(toAddressDto(delivery.getAddress()))
                .fromAddress(toAddressDto(delivery.getFromAddress()))
                .state(delivery.getState().name())
                .orderId(delivery.getOrderId())
                .fragile(delivery.isFragile())
                .weight(delivery.getWeight())
                .volume(delivery.getVolume())
                .build();
    }

    public Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                .id(dto.getId())
                .address(toAddress(dto.getAddress()))
                .fromAddress(toAddress(dto.getFromAddress()))
                .state(DeliveryState.valueOf(dto.getState()))
                .orderId(dto.getOrderId())
                .fragile(dto.isFragile())
                .weight(dto.getWeight())
                .volume(dto.getVolume())
                .build();
    }

    private AddressDto toAddressDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    private Address toAddress(AddressDto dto) {
        return Address.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .street(dto.getStreet())
                .house(dto.getHouse())
                .flat(dto.getFlat())
                .build();
    }
}
