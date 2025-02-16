package com.order.exception.exceptionHandlingInSpringboot.localExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalExceptionHandlingInController {

    /*@GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return new User(id, "John Doe");
    }*/

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
