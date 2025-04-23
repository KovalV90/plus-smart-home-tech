package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.ShoppingCartClient;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderState;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ShoppingCartClient shoppingCartClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;


    @Override
    public List<OrderDto> getClientOrders(String username) {
        return repository.findAllByUsername(username)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        UUID cartId = request.getShoppingCart().getShoppingCartId();
        ShoppingCartDto cart = shoppingCartClient.getShoppingCartById(cartId);

        // Создаём первоначальный заказ
        Order order = Order.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(cart.getProducts())
                .deliveryAddress(String.valueOf(request.getDeliveryAddress()))
                .state(OrderState.NEW.name())
                .username(request.getUsername())
                .build();

        // Сохраняем заказ, чтобы получить orderId
        order = repository.save(order);
        log.info("Создан черновик заказа: {}", order.getOrderId());
// Запрашиваем стоимость товаров
        Double productPrice = paymentClient.calculateProductCost(order.getOrderId());
        order.setProductPrice(productPrice);
        log.info("Рассчитана стоимость товаров: {}", productPrice);

        // Запрашиваем стоимость доставки
        Double deliveryPrice = deliveryClient.calculateDeliveryCost(order.getOrderId());
        order.setDeliveryPrice(deliveryPrice);
        log.info("Рассчитана стоимость доставки: {}", deliveryPrice);

        // Создаём платёж
        PaymentDto payment = PaymentDto.builder()
                .amount(order.getProductPrice())
                .deliveryPrice(deliveryPrice)
                .username(order.getUsername())
                .orderId(order.getOrderId())
                .build();
        payment = paymentClient.createPayment(payment);
        order.setPaymentId(payment.getPaymentId());
        log.info("Создан платёж: {}", payment.getPaymentId());
        // Создаём доставку
        DeliveryDto delivery = DeliveryDto.builder()
                .address(request.getDeliveryAddress())
                .orderId(order.getOrderId())
                .build();
        delivery = deliveryClient.createDelivery(delivery);
        order.setDeliveryId(delivery.getId());
        log.info("Создана доставка: {}", delivery.getId());
        // Обновляем заказ с paymentId и deliveryId
        Order savedOrder = repository.save(order);

        // Деактивируем корзину
        shoppingCartClient.deactivateShoppingCart(cartId);
        log.info("Окончательный заказ сохранён: {}", savedOrder.getOrderId());

        return mapper.toDto(savedOrder);
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
        Double deliveryPrice = deliveryClient.calculateDeliveryCost(orderId);
        order.setDeliveryPrice(deliveryPrice);
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

    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        return updateState(orderId, OrderState.PAID);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        return updateState(orderId, OrderState.PAYMENT_FAILED);
    }
    @Override
    public OrderDto cancelOrder(UUID orderId) {
        return updateState(orderId, OrderState.CANCELED);
    }


}
