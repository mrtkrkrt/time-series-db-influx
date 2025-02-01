package com.mrtkrkrt.time_series.stock.adapter.cron;

import com.mrtkrkrt.time_series.stock.service.CurrencyUpdateHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyScheduler {

    private final CurrencyUpdateHandler currencyUpdateHandler;

    @PostConstruct
    public void fetchCurrency() {
        log.info("Fetching currency data");
        currencyUpdateHandler.fetchAndUpdateCurrency();
    }
}
