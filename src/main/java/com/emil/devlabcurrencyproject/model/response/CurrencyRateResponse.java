package com.emil.devlabcurrencyproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateResponse {

    private boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates = new HashMap<>();

}
