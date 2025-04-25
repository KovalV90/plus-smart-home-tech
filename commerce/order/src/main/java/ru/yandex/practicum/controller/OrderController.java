package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.service.OrderService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {

    private final OrderService orderService;

    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam String username) {
        return orderService.getClientOrders(username);
    }

    @Override
    @PutMapping
    public OrderDto createOrder(@RequestBody CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnProducts(@RequestBody ProductReturnRequest request) {
        return orderService.returnProducts(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto markAsPaid(@RequestBody UUID orderId) {
        return orderService.markAsPaid(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto markAsPaymentFailed(@RequestBody UUID orderId) {
        return orderService.markAsPaymentFailed(orderId);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto markAsDelivered(@RequestBody UUID orderId) {
        return orderService.markAsDelivered(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto markAsDeliveryFailed(@RequestBody UUID orderId) {
        return orderService.markAsDeliveryFailed(orderId);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody UUID orderId) {
        return orderService.completeOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID orderId) {
        return orderService.calculateTotalCost(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        return orderService.calculateDeliveryCost(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto markAsAssembled(@RequestBody UUID orderId) {
        return orderService.markAsAssembled(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto markAsAssemblyFailed(@RequestBody UUID orderId) {
        return orderService.markAsAssemblyFailed(orderId);
    }

    @Override
    @PostMapping("/payment/success")
    public OrderDto paymentSuccess(@RequestBody UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }
    @Override
    @PostMapping("/payment/failure")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }


    @Override
    @PostMapping("/cancel")
    public OrderDto cancelOrder(@RequestBody UUID orderId) {
        return orderService.cancelOrder(orderId);
    }


}
