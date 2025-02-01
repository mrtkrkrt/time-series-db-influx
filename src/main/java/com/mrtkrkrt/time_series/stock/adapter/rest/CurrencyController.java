package com.mrtkrkrt.time_series.stock.adapter.rest;

import com.mrtkrkrt.time_series.stock.adapter.rest.dto.query.ChartDataResponse;
import com.mrtkrkrt.time_series.stock.service.CurrencyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stock")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyHandler currencyHandler;

    @GetMapping
    public ResponseEntity<ChartDataResponse> getLast5MinutesResponse() {
        return ResponseEntity.ok(currencyHandler.getLast5MinutesResponse());
    }
}
