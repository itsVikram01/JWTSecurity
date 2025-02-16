package com.order.exception.exceptionHandlingInSpringboot.usingResponseStatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResponseStatusExample extends RuntimeException {
    public ResponseStatusExample(String message) {
        super(message);
    }
}
