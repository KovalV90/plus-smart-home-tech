package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class DeactivatedShoppingCartException extends RuntimeException {
    public DeactivatedShoppingCartException(String message) {
        super(message);
    }
}