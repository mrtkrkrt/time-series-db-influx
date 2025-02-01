package com.mrtkrkrt.time_series.stock.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrtkrkrt.time_series.stock.service.influx.InfluxCurrencyHandler;
import com.mrtkrkrt.time_series.stock.service.model.query.ExchangeRate;
import com.mrtkrkrt.time_series.stock.service.model.query.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyUpdateHandler {

    private final InfluxCurrencyHandler currencyHandler;

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";

    public void fetchAndUpdateCurrency() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);

            String jsonResponse = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateResponse apiResponse = objectMapper.readValue(jsonResponse, ExchangeRateResponse.class);

            ExchangeRate exchangeRate = new ExchangeRate("EUR", apiResponse.rates().get("EUR"));

            currencyHandler.updateCurrency(exchangeRate);
        } catch (Exception e) {
            log.error("Error while fetching currency data", e);
        }
    }
}
