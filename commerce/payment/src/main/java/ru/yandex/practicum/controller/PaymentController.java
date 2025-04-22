package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @PostMapping("/{id}/paid")
    public ResponseEntity<PaymentDto> markAsPaid(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.markAsPaid(id));
    }

    @PostMapping("/{id}/failed")
    public ResponseEntity<PaymentDto> markAsFailed(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.markAsFailed(id));
    }
}
