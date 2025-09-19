package com.emil.devlabcurrencyproject.controller;

import com.emil.devlabcurrencyproject.model.response.CurrencyRateResponse;
import com.emil.devlabcurrencyproject.model.response.InfoResponse;
import com.emil.devlabcurrencyproject.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/rates")
    public ResponseEntity<?> getCurrencyRates() {
        CurrencyRateResponse currencyRateResponse = currencyService.getCurrencyRates();
        return ResponseEntity.status(HttpStatus.OK)
                .body(InfoResponse.builder()
                        .success(true)
                        .message("Currency rates retrieved successfully")
                        .data(currencyRateResponse)
                        .build());
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(@RequestParam(value = "base", required = false, defaultValue = "USD") String base,
                                             @RequestParam(value = "to", required = true) String to,
                                             @RequestParam(value = "amount", required = true) BigDecimal amount) {
        BigDecimal convertedAmount = currencyService.convertCurrency(base, to, amount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(InfoResponse.builder()
                        .success(true)
                        .message("Currency converted successfully")
                        .data(convertedAmount)
                        .build());

    }

}