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

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @PostMapping("/productCost")
    public ResponseEntity<Double> calculateProductCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(paymentService.calculateProductCost(orderId));
    }


    @PostMapping("/totalCost")
    public ResponseEntity<Double> calculateTotalCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(paymentService.calculateTotalCost(orderId));
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentDto> paymentSuccess(@RequestBody UUID paymentId) {
        return ResponseEntity.ok(paymentService.paymentSuccess(paymentId));
    }

    @PostMapping("/failed")
    public ResponseEntity<PaymentDto> paymentFailed(@RequestBody UUID paymentId) {
        return ResponseEntity.ok(paymentService.paymentFailed(paymentId));
    }
}
