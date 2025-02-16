package com.order.exception.exceptionHandlingInSpringboot.usingRestControllerAdvice;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
