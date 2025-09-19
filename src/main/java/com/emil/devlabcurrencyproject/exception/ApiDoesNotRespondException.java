package com.emil.devlabcurrencyproject.exception;

public class ApiDoesNotRespondException extends RuntimeException {
    public ApiDoesNotRespondException(String message) {
        super(message);
    }
}
