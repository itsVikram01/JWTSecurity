package com.order.exception.exceptionHandlingInSpringboot.usingRestControllerAdvice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

}
