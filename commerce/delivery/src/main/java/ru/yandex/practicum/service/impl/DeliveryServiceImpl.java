package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.exception.DeliveryNotFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryState;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;

    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        Delivery delivery = mapper.toEntity(dto);
        delivery.setState(DeliveryState.CREATED);
        return mapper.toDto(repository.save(delivery));
    }

    @Override
    public DeliveryDto markDelivered(UUID id) {
        Delivery delivery = findByIdOrThrow(id);
        delivery.setState(DeliveryState.DELIVERED);
        return mapper.toDto(repository.save(delivery));
    }

    @Override
    public DeliveryDto markFailed(UUID id) {
        Delivery delivery = findByIdOrThrow(id);
        delivery.setState(DeliveryState.FAILED);
        return mapper.toDto(repository.save(delivery));
    }
    @Override
    public DeliveryDto markInProgress(UUID id) {
        Delivery delivery = findByIdOrThrow(id);
        delivery.setState(DeliveryState.IN_PROGRESS);
        return mapper.toDto(repository.save(delivery));
    }

    @Override
    public DeliveryDto cancelDelivery(UUID id) {
        Delivery delivery = findByIdOrThrow(id);
        delivery.setState(DeliveryState.CANCELLED);
        return mapper.toDto(repository.save(delivery));
    }

    @Override
    public Double calculateDeliveryCost(UUID orderId) {
        Delivery delivery = repository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery не найдена по orderId: " + orderId));

        double base = 5.0;

        String fromStreet = delivery.getFromAddress().getStreet();
        String toStreet = delivery.getAddress().getStreet();

        double fromFactor = fromStreet.contains("ADDRESS_2") ? 2.0 : 1.0;
        double result = base * fromFactor + base;

        if (delivery.isFragile()) {
            result += result * 0.2;
        }

        result += delivery.getWeight() * 0.3;
        result += delivery.getVolume() * 0.2;

        if (!toStreet.equalsIgnoreCase(fromStreet)) {
            result += result * 0.2;
        }

        return result;
    }
    private Delivery findByIdOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery не найдена(id): " + id));
    }
}
