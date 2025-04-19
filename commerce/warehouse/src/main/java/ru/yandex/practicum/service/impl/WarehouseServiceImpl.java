package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.DuplicateProductException;
import ru.yandex.practicum.exception.NotEnoughProductQuantityException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.service.WarehouseService;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ShoppingStoreClient shoppingStoreClient;

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    @Transactional
    public void newProductInWarehouse(AddNewProductInWarehouseRequest request) {
        log.info("Запуск операции добавления нового товара на склад: request = {}", request);
        checkProductAlreadyExist(request.getProductId());
        WarehouseProduct warehouseProduct = WarehouseMapper.toWarehouseProduct(request);
        warehouseRepository.save(warehouseProduct);
        log.info("Новый товар успешно добавлен на склад: productId = {}", request.getProductId());
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart) {
        log.info("Запуск проверки остатков для корзины: cart = {}", cart);
        Map<UUID, Long> cartProductsMap = cart.getProducts();
        List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(cartProductsMap.keySet());
        Map<UUID, Long> warehouseProductsMap = warehouseProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, WarehouseProduct::getQuantity));

        List<String> exceptionMessages = cartProductsMap.entrySet()
                .stream()
                .filter(entry -> warehouseProductsMap.getOrDefault(entry.getKey(), 0L) < entry.getValue())
                .map(entry -> String.format("Недостаток товара с id = %s, не хватает %d шт.",
                        entry.getKey(),
                        entry.getValue() - warehouseProductsMap.getOrDefault(entry.getKey(), 0L)))
                .toList();

        if (!exceptionMessages.isEmpty()) {
            log.error("Ошибка проверки: {}", exceptionMessages);
            throw new NotEnoughProductQuantityException(exceptionMessages.toString());
        }

        BookedProductsDto bookedProductsDto = calculateDeliveryParams(warehouseProducts);
        log.info("Проверка остатков завершена, рассчитанные параметры доставки: {}", bookedProductsDto);
        return bookedProductsDto;
    }

    @Override
    @Transactional
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Запуск пополнения остатков на складе: request = {}", request);
        WarehouseProduct warehouseProduct = checkWarehouseProductExist(request.getProductId());
        Long newQuantity = warehouseProduct.getQuantity() + request.getQuantity();
        warehouseProduct.setQuantity(newQuantity);
        warehouseRepository.save(warehouseProduct);
        log.info("Остатки товара обновлены: новый остаток = {} для productId = {}", newQuantity, request.getProductId());
        updateProductQuantityInShoppingStore(warehouseProduct);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.info("Запуск получения адреса склада");
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry(CURRENT_ADDRESS);
        addressDto.setCity(CURRENT_ADDRESS);
        addressDto.setStreet(CURRENT_ADDRESS);
        addressDto.setHouse(CURRENT_ADDRESS);
        addressDto.setFlat(CURRENT_ADDRESS);
        log.info("Получен адрес склада: {}", addressDto);
        return addressDto;
    }

    private void checkProductAlreadyExist(UUID productId) {
        warehouseRepository.findById(productId).ifPresent(product -> {
            log.warn("Проверка: товар с id = {} уже присутствует на складе", productId);
            throw new DuplicateProductException("Товар с id = " + productId + " уже представлен на складе");
        });
    }

    private BookedProductsDto calculateDeliveryParams(List<WarehouseProduct> warehouseProducts) {
        double totalWeight = warehouseProducts.stream()
                .mapToDouble(WarehouseProduct::getWeight)
                .sum();
        double totalVolume = warehouseProducts.stream()
                .mapToDouble(product -> product.getWidth() * product.getHeight() * product.getDepth())
                .sum();
        boolean hasFragileProducts = warehouseProducts.stream()
                .anyMatch(WarehouseProduct::isFragile);

        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setDeliveryWeight(totalWeight);
        bookedProductsDto.setDeliveryVolume(totalVolume);
        bookedProductsDto.setFragile(hasFragileProducts);

        return bookedProductsDto;
    }

    private WarehouseProduct checkWarehouseProductExist(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> {
                    String errMsg = "Товар с id = " + productId + " отсутствует на складе";
                    log.error(errMsg);
                    return new NotFoundException(errMsg);
                });
    }

    private void updateProductQuantityInShoppingStore(WarehouseProduct product) {
        Long quantity = product.getQuantity();
        QuantityState quantityState;

        if (quantity == 0) {
            quantityState = QuantityState.ENDED;
        } else if (quantity < 10) {
            quantityState = QuantityState.FEW;
        } else if (quantity < 100 && quantity >= 10) {
            quantityState = QuantityState.ENOUGH;
        } else {
            quantityState = QuantityState.MANY;
        }

        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest();
        request.setProductId(product.getProductId());
        request.setQuantityState(quantityState);

        log.info("Обновление статуса количества товара в shopping-store: productId = {}, новый статус = {}", product.getProductId(), quantityState);
        shoppingStoreClient.setProductQuantityState(request);
    }
}
