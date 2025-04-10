package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartState;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.info("Получение корзины пользователя: {}", username);
        ShoppingCart cart = shoppingCartRepository.findByUserName(username)
                .orElseGet(() -> {
                    log.info("Корзина не найдена, создается новая для пользователя: {}", username);
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUserName(username);
                    newCart.setProducts(new HashMap<>());
                    newCart.setState(ShoppingCartState.ACTIVE);
                    return shoppingCartRepository.save(newCart);
                });

        ShoppingCartDto cartDto = ShoppingCartMapper.toShoppingCartDto(cart);
        log.info("Корзина найдена/создана: {}", cartDto);
        return cartDto;
    }

    @Transactional
    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        log.info("Добавление товаров {} в корзину пользователя: {}", products, username);
        ShoppingCart cart = findActiveCart(username);

        products.forEach((productId, quantity) ->
                cart.getProducts().merge(productId, quantity, Long::sum)
        );


        ShoppingCartDto dtoForCheck = ShoppingCartMapper.toShoppingCartDto(cart);


        try {
            log.info("Проверка доступности товаров на складе через warehouseClient");
            warehouseClient.checkProductQuantityEnoughForShoppingCart(dtoForCheck);
            log.info("Проверка прошла успешно — товары доступны");
        } catch (Exception e) {
            log.error("Ошибка при проверке наличия товаров на складе: {}", e.getMessage());
            throw new RuntimeException("Недостаточное количество товаров на складе: " + e.getMessage(), e);
        }


        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        ShoppingCartDto response = ShoppingCartMapper.toShoppingCartDto(savedCart);
        log.info("Товары успешно добавлены. Обновленная корзина: {}", response);
        return response;
    }


    @Transactional
    @Override
    public void deactivateCurrentShoppingCart(String username) {
        log.info("Деактивация корзины пользователя: {}", username);
        ShoppingCart cart = findActiveCart(username);
        cart.setState(ShoppingCartState.DEACTIVATE);
        shoppingCartRepository.save(cart);
        log.info("Корзина пользователя {} успешно деактивирована", username);
    }

    @Transactional
    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        log.info("Удаление товаров {} из корзины пользователя {}", productIds, username);
        ShoppingCart cart = findActiveCart(username);

        boolean anyRemoved = productIds.removeIf(id -> cart.getProducts().remove(id) == null);

        if (!anyRemoved) {
            log.warn("Ни один из указанных продуктов не найден в корзине пользователя: {}", username);
        }

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        ShoppingCartDto response = ShoppingCartMapper.toShoppingCartDto(updatedCart);
        log.info("Корзина после удаления товаров: {}", response);
        return response;
    }

    @Transactional
    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        UUID productId = request.getProductId();
        Long newQuantity = request.getQuantity();
        log.info("Изменение количества продукта {} на {} в корзине пользователя {}", productId, newQuantity, username);

        ShoppingCart cart = findActiveCart(username);

        if (!cart.getProducts().containsKey(productId)) {
            throw new NotFoundException("Продукт с id=" + productId + " не найден в корзине");
        }

        cart.getProducts().put(productId, newQuantity);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        ShoppingCartDto response = ShoppingCartMapper.toShoppingCartDto(savedCart);
        log.info("Количество обновлено. Текущее состояние корзины: {}", response);
        return response;
    }

    private ShoppingCart findActiveCart(String username) {
        return shoppingCartRepository.findByUserName(username)
                .filter(cart -> cart.getState() == ShoppingCartState.ACTIVE)
                .orElseThrow(() -> {
                    String msg = "Активная корзина для пользователя " + username + " не найдена";
                    log.error(msg);
                    return new NotFoundException(msg);
                });
    }
}
