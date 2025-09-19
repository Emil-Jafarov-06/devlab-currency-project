package com.emil.devlabcurrencyproject.exception;

import com.emil.devlabcurrencyproject.model.response.InfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiUnavailableException.class)
    public ResponseEntity<InfoResponse<String>> handleException(ApiUnavailableException e) {
        return ResponseEntity.status(HttpStatus
                .PROCESSING)
                .body(InfoResponse.<String>builder()
                        .success(false)
                        .message(e.getMessage())
                        .data(null).build());
    }

}
