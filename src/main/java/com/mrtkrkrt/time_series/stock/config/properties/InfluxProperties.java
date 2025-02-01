package com.mrtkrkrt.time_series.stock.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "influx")
public class InfluxProperties {
    @NotNull
    private String url;
    @NotNull
    private String token;
    @NotNull
    private String org;
    @NotNull
    private String bucket;
}
