package ru.yandex.practicum.service;


import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> getClientOrders(String username);
    OrderDto createOrder(CreateNewOrderRequest request);
    OrderDto returnProducts(ProductReturnRequest request);
    OrderDto markAsPaid(UUID orderId);
    OrderDto markAsPaymentFailed(UUID orderId);
    OrderDto markAsDelivered(UUID orderId);
    OrderDto markAsDeliveryFailed(UUID orderId);
    OrderDto completeOrder(UUID orderId);
    OrderDto calculateTotalCost(UUID orderId);
    OrderDto calculateDeliveryCost(UUID orderId);
    OrderDto markAsAssembled(UUID orderId);
    OrderDto markAsAssemblyFailed(UUID orderId);
    OrderDto paymentSuccess(UUID orderId);
    OrderDto paymentFailed(UUID orderId);
    OrderDto cancelOrder(UUID orderId);

}
