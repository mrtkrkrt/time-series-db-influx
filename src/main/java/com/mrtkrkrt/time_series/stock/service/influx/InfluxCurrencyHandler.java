package com.mrtkrkrt.time_series.stock.service.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.mrtkrkrt.time_series.stock.service.model.query.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxCurrencyHandler {

    private final InfluxDBClient influxDBClient;

    public void updateCurrency(ExchangeRate exchangeRate) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Point point = Point.measurement("exchange_rate")
                .addTag("currency", exchangeRate.currency())
                .addField("rate", exchangeRate.rate())
                .addField("time", System.currentTimeMillis())
                .time(System.currentTimeMillis(), WritePrecision.MS);

        writeApi.writePoint(point);
        System.out.println("Dolar kuru InfluxDB'ye kaydedildi: " + exchangeRate.rate());
    }
}
