package com.deliverytech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para ser lançada quando uma entidade não é encontrada.
 * A anotação @ResponseStatus(HttpStatus.NOT_FOUND) garante que, por padrão,
 * qualquer controller que não capture esta exceção retornará um código 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}