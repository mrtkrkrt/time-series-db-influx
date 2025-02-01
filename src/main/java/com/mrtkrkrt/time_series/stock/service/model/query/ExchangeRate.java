package com.mrtkrkrt.time_series.stock.service.model.query;

import lombok.Builder;

@Builder
public record ExchangeRate(
        String currency,
        double rate
) {
}
