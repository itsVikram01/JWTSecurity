package com.apps.exception;

public class DuplicateAppException extends RuntimeException {
    public DuplicateAppException(String message) {
        super(message);
    }
}