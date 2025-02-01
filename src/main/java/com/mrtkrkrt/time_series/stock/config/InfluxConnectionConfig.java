package com.mrtkrkrt.time_series.stock.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.mrtkrkrt.time_series.stock.config.properties.InfluxProperties;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InfluxConnectionConfig {

    private final InfluxProperties influxProperties;
    private InfluxDBClient influxDBClient;

    @Bean
    public InfluxDBClient influxDBClient() {
        influxDBClient = InfluxDBClientFactory.create(
                influxProperties.getUrl(),
                influxProperties.getToken().toCharArray(),
                influxProperties.getOrg(),
                influxProperties.getBucket());
        return influxDBClient;
    }

    @PreDestroy
    public void closeInfluxDBClient() {
        if (influxDBClient != null) {
            influxDBClient.close();
        }
    }
}
