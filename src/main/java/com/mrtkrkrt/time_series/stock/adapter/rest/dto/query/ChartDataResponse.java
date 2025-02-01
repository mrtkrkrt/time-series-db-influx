package com.mrtkrkrt.time_series.stock.adapter.rest.dto.query;

import java.math.BigDecimal;
import java.util.List;

public record ChartDataResponse(
        List<BigDecimal> prices
) {
}
