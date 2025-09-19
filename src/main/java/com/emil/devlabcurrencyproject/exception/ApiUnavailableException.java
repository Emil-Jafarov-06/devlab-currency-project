package com.emil.devlabcurrencyproject.exception;

public class ApiUnavailableException extends RuntimeException {
    public ApiUnavailableException(String message) {
        super(message);
    }
}
