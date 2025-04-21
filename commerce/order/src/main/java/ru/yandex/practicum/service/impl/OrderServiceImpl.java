package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderState;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        return repository.findAllByUsername(username)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        Order order = Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .deliveryAddress(request.getDeliveryAddress().toString())
                .state(OrderState.NEW.name())
                .username(request.getUsername())
                .build();
        return mapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        return updateState(request.getOrderId(), OrderState.PRODUCT_RETURNED);
    }

    @Override
    public OrderDto markAsPaid(UUID orderId) {
        return updateState(orderId, OrderState.PAID);
    }

    @Override
    public OrderDto markAsPaymentFailed(UUID orderId) {
        return updateState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto markAsDelivered(UUID orderId) {
        return updateState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto markAsDeliveryFailed(UUID orderId) {
        return updateState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return updateState(orderId, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = findById(orderId);
        order.setTotalPrice(safe(order.getProductPrice()) + safe(order.getDeliveryPrice()));
        return mapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = findById(orderId);
        order.setDeliveryPrice(500.0); // ЗАГЛУШКА
        return mapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto markAsAssembled(UUID orderId) {
        return updateState(orderId, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto markAsAssemblyFailed(UUID orderId) {
        return updateState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private Order findById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new NoOrderFoundException("Order c id не найден: " + id));
    }

    private OrderDto updateState(UUID id, OrderState state) {
        Order order = findById(id);
        order.setState(state.name());
        return mapper.toDto(repository.save(order));
    }

    private double safe(Double val) {
        return val != null ? val : 0.0;
    }
}
