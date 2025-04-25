package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {

    private final DeliveryService service;

    @Override
    @PutMapping
    public DeliveryDto createDelivery(@RequestBody DeliveryDto dto) {
        return service.createDelivery(dto);
    }

    @Override
    @PostMapping("/successful")
    public DeliveryDto markDelivered(@RequestBody UUID id) {
        return service.markDelivered(id);
    }

    @Override
    @PostMapping("/failed")
    public DeliveryDto markFailed(@RequestBody UUID id) {
        return service.markFailed(id);
    }

    @Override
    @PostMapping("/cost")
    public Double calculateDeliveryCost(@RequestBody OrderDto orderDto) {
        return service.calculateDeliveryCost(orderDto);
    }

    @Override
    @PostMapping("/picked")
    public DeliveryDto markPicked(@RequestBody UUID id) {
        return service.markInProgress(id);
    }
}
