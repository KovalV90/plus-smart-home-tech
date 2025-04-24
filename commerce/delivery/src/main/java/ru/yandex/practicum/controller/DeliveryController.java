package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @PutMapping
    public ResponseEntity<DeliveryDto> create(@RequestBody DeliveryDto dto) {
        return ResponseEntity.ok(service.createDelivery(dto));
    }

    @PostMapping("/successful")
    public ResponseEntity<DeliveryDto> success(@RequestBody UUID id) {
        return ResponseEntity.ok(service.markDelivered(id));
    }

    @PostMapping("/failed")
    public ResponseEntity<DeliveryDto> fail(@RequestBody UUID id) {
        return ResponseEntity.ok(service.markFailed(id));
    }

    @GetMapping("/cost")
    public ResponseEntity<Double> cost(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(service.calculateDeliveryCost(orderDto));
    }

    @PostMapping("/piked")
    public ResponseEntity<DeliveryDto> picked(@RequestBody UUID id) {
        return ResponseEntity.ok(service.markInProgress(id));
    }

}
