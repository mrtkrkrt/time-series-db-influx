package com.mrtkrkrt.time_series.stock.service.model.query;

import java.util.Map;

public record ExchangeRateResponse(
        String provider,
        String base,
        String date,
        String terms,
        String WARNING_UPGRADE_TO_V6,
        Map<String, Double> rates,
        Integer time_last_updated
) {
}
