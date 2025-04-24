package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderApi {

    OrderDto markAsPaid(UUID orderId);

    OrderDto markAsPaymentFailed(UUID orderId);

    OrderDto markAsDelivered(UUID orderId);

    OrderDto markAsDeliveryFailed(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    List<OrderDto> getClientOrders(String username);

    OrderDto createOrder(CreateNewOrderRequest request);
    OrderDto returnProducts(ProductReturnRequest request);
    OrderDto calculateTotalCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto markAsAssembled(UUID orderId);

    OrderDto markAsAssemblyFailed(UUID orderId);

    OrderDto paymentSuccess(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto cancelOrder(UUID orderId);
}
