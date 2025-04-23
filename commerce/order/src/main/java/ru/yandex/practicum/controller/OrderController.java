package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.OrderService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getClientOrders(@RequestParam String username) {
        return ResponseEntity.ok(orderService.getClientOrders(username));
    }

    @PutMapping
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody CreateNewOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/return")
    public ResponseEntity<OrderDto> productReturn(@RequestBody ProductReturnRequest request) {
        return ResponseEntity.ok(orderService.returnProducts(request));
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDto> payment(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsPaid(orderId));
    }

    @PostMapping("/payment/failed")
    public ResponseEntity<OrderDto> paymentFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsPaymentFailed(orderId));
    }

    @PostMapping("/delivery")
    public ResponseEntity<OrderDto> delivery(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsDelivered(orderId));
    }

    @PostMapping("/delivery/failed")
    public ResponseEntity<OrderDto> deliveryFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsDeliveryFailed(orderId));
    }

    @PostMapping("/completed")
    public ResponseEntity<OrderDto> complete(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }

    @PostMapping("/calculate/total")
    public ResponseEntity<OrderDto> calculateTotalCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.calculateTotalCost(orderId));
    }

    @PostMapping("/calculate/delivery")
    public ResponseEntity<OrderDto> calculateDeliveryCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.calculateDeliveryCost(orderId));
    }

    @PostMapping("/assembly")
    public ResponseEntity<OrderDto> assembly(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsAssembled(orderId));
    }

    @PostMapping("/assembly/failed")
    public ResponseEntity<OrderDto> assemblyFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.markAsAssemblyFailed(orderId));
    }
    @PostMapping("/payment/success")
    public ResponseEntity<OrderDto> paymentSuccess(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.paymentSuccess(orderId));
    }
    @PostMapping("/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }


}
