package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @PostMapping
    public ResponseEntity<DeliveryDto> create(@RequestBody DeliveryDto dto) {
        return ResponseEntity.ok(service.createDelivery(dto));
    }

    @PostMapping("/{id}/success")
    public ResponseEntity<DeliveryDto> success(@PathVariable UUID id) {
        return ResponseEntity.ok(service.markDelivered(id));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<DeliveryDto> fail(@PathVariable UUID id) {
        return ResponseEntity.ok(service.markFailed(id));
    }
    @GetMapping("/cost/{orderId}")
    public ResponseEntity<Double> getCost(@PathVariable UUID orderId) {
        return ResponseEntity.ok(service.calculateDeliveryCost(orderId));
    }

}
