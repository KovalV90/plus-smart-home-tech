package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient {


    @PostMapping("/api/v1/order/payment")
    OrderDto markAsPaid(@RequestBody UUID orderId);


    @PostMapping("/api/v1/order/payment/failed")
    OrderDto markAsPaymentFailed(@RequestBody UUID orderId);


    @PostMapping("/api/v1/order/delivery")
    OrderDto markAsDelivered(@RequestBody UUID orderId);


    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto markAsDeliveryFailed(@RequestBody UUID orderId);


    @PostMapping("/api/v1/order/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/success")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/failure")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @GetMapping("/api/v1/order")
    List<OrderDto> getClientOrders(@RequestParam String username);

    @PutMapping("/api/v1/order")
    OrderDto createOrder(@RequestBody CreateNewOrderRequest request);

    @PostMapping("/api/v1/order/return")
    OrderDto returnProducts(@RequestBody ProductReturnRequest request);

    @PostMapping("/api/v1/order/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/assembly")
    OrderDto markAsAssembled(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto markAsAssemblyFailed(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/cancel")
    OrderDto cancelOrder(@RequestBody UUID orderId);

    @GetMapping("/api/v1/order/{orderId}")
    OrderDto getOrderById(@PathVariable("orderId") UUID orderId);

}
