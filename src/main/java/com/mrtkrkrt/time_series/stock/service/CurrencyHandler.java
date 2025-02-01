package com.mrtkrkrt.time_series.stock.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.mrtkrkrt.time_series.stock.adapter.rest.dto.query.ChartDataResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyHandler {

    private final InfluxDBClient influxDBClient;

//    @PostConstruct
    public void saveStock() {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        Point point = Point.measurement("exchange_rate")
                .addTag("stock", "TSLA")
                .addField("price", 700.0)
                .time(System.currentTimeMillis(), WritePrecision.MS);
        writeApi.writePoint(point);
    }

    public ChartDataResponse getLast5MinutesResponse() {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = "from(bucket: \"" + "mybucket" + "\")\n" +
                "  |> range(start: -5m)\n" +
                "  |> filter(fn: (r) => r._measurement == \"exchange_rate\" and r.currency == \"EUR\")\n" +
                "  |> filter(fn: (r) => r._field == \"rate\" or r._field == \"time\")";

        List<FluxTable> tables = queryApi.query(fluxQuery);
        List<BigDecimal> prices = new ArrayList<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String field = record.getField();
                System.out.println("Time: " + record.getTime() +
                        ", Currency: " + record.getValueByKey("currency") +
                        ", Rate: " + record.getValue());
                prices.add(record.getValue() != null ? new BigDecimal(record.getValue().toString()) : BigDecimal.ZERO);
            }
        }
        return new ChartDataResponse(prices);
    }
}
