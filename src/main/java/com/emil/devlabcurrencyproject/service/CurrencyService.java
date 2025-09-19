package com.emil.devlabcurrencyproject.service;

import com.emil.devlabcurrencyproject.exception.ApiDoesNotRespondException;
import com.emil.devlabcurrencyproject.exception.ApiUnavailableException;
import com.emil.devlabcurrencyproject.model.response.CurrencyRateResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.number.money.CurrencyUnitFormatter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${api.key}")
    String accessKey;
    String api = "http://data.fixer.io/api/";
    Gson gson = new Gson();

    private final RestTemplate restTemplate;
    private final CacheManager cacheManager;


    @Scheduled(cron = "0 0 * * * * ")
    @Retryable(retryFor = ApiDoesNotRespondException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    private void refreshCache() {
        String base = "EUR";
        String apiContinuation = "latest?access_key=" + accessKey + "&format=1";
        String apiResponse = restTemplate.getForObject(api + apiContinuation, String.class);
        if (apiResponse == null) throw new ApiDoesNotRespondException("API response is null");
        Objects.requireNonNull(cacheManager.
                getCache("currencyRates")).
                put("latest", gson.fromJson(apiResponse, CurrencyRateResponse.class));
    }

    /*
    @Recover
    public CurrencyRateResponse recoverCurrencyInfo(ApiDoesNotRespondException e) {
        log.warn("API does not respond, using cached data!");
        return (CurrencyRateResponse) Objects.requireNonNull(cacheManager.
                getCache("currencyRates")).
                get("latest").
                get();
    }
     */

    public CurrencyRateResponse getCurrencyRates() {
        var cache = Objects.requireNonNull(cacheManager.getCache("currencyRates")).get("latest");;

        if (cache != null) {
            return (CurrencyRateResponse) cache.get();
        }

        try {
            refreshCache();
            var updatedCache = Objects.requireNonNull(cacheManager.getCache("currencyRates")).get("latest");
            System.out.println((CurrencyRateResponse) updatedCache.get());
            if (updatedCache != null) {
                return (CurrencyRateResponse) updatedCache.get();
            }
        } catch (RuntimeException e) {
            log.error("Failed to refresh cache", e);
        }

        throw new ApiUnavailableException("No data in the cache or external API unavailable. Please try again later.");
    }



    public BigDecimal convertCurrency(String base, String to, BigDecimal amount) {
        CurrencyRateResponse currencyRateResponse = getCurrencyRates();
        BigDecimal eurToBaseRate = currencyRateResponse.getRates().get(base);
        BigDecimal eurToTargetRate = currencyRateResponse.getRates().get(to);
        return amount.divide(eurToBaseRate, 7, RoundingMode.CEILING).multiply(eurToTargetRate);
    }

}
