# InfluxDB Time Series Data Management

This project demonstrates how to write and query time-series data using **InfluxDB**. It also covers how to integrate InfluxDB with a **Spring Boot** application for basic data insertion and retrieval.

## Table of Contents

1. [What is InfluxDB?](#what-is-influxdb)
2. [InfluxDB Basic Structure](#influxdb-basic-structure)
3. [Setting Up InfluxDB](#setting-up-influxdb)
4. [Points, Fields, and Tags](#points-fields-and-tags)
5. [Inserting Data into InfluxDB with Spring Boot](#inserting-data-into-influxdb-with-spring-boot)

## What is InfluxDB?

**InfluxDB** is an open-source time-series database designed to handle large amounts of time-stamped data. It is commonly used to store data such as:

- Sensor data (e.g., temperature, humidity)
- Financial data (e.g., stock prices)
- Server monitoring data (e.g., CPU usage, network traffic)

InfluxDB is optimized for fast writes and queries, making it ideal for handling time-series data.

### InfluxDB Basic Structure

The basic structure of InfluxDB consists of the following components:

- **Measurement**: The category of data. For example, a measurement could be `temperature` for a temperature reading.
- **Field**: The actual value of the data. For example, the field could be `temperature` (e.g., `22.5` degrees).
- **Tag**: A key-value pair used to identify and index the data, enabling fast querying. For example, a tag could be `location` (e.g., `NYC`).
- **Time**: The timestamp associated with the data.

These components help to define how InfluxDB stores and queries time-series data.

## Setting Up InfluxDB

The following shows how to quickly set up **InfluxDB** using **Docker Compose**. This will set up the InfluxDB instance for local development.

### Docker Compose Configuration

```yaml
version: '3.8'

services:
  influxdb:
    image: influxdb:2.7
    container_name: influxdb
    ports:
      - "8086:8086"
    volumes:
      - influxdb-data:/var/lib/influxdb2
    environment:
      - DOCKER_INFLUXDB_INIT_MODE=setup
      - DOCKER_INFLUXDB_INIT_USERNAME=admin
      - DOCKER_INFLUXDB_INIT_PASSWORD=admin123
      - DOCKER_INFLUXDB_INIT_ORG=myorg
      - DOCKER_INFLUXDB_INIT_BUCKET=mybucket
      - DOCKER_INFLUXDB_INIT_ADMIN_TOKEN=mytoken
    restart: unless-stopped

volumes:
  influxdb-data:
```

### Starting InfluxDB with Docker Compose
1. Save the above Docker Compose configuration as docker-compose.yml.
2. Run the following command to start InfluxDB:
```bash
docker-compose up -d
```
3. InfluxDB will be available at localhost:8086. To access the web UI, open your browser and go to http://localhost:8086. Use the credentials (admin / admin123) to log in.

## Points, Fields, and Tags
### What is a Point?
A Point is the basic structure in InfluxDB that contains a measurement, field, tag, and time. Each point represents a time-series data entry.
For example, when recording a stock price:

- Measurement: `stock_price`
- Field: `price` (e.g., `100.0`)
- Tag: `symbol` (e.g., `AAPL`)
- Time: `2022-01-01T00:00:00Z`

### What is a Field?
A Field holds the actual value of the data. Fields can be numeric, string, or boolean. For example, temperature can be a field that stores the temperature value (e.g., 22.5 degrees). Fields are not indexed and are stored efficiently for fast writes.

### What is a Tag?
Tags are key-value pairs used to categorize and index the data, which makes querying more efficient. Tags are indexed, so queries that involve tags will be much faster. However, tags should not have too many unique values.

### Time (Timestamp)
Time is the timestamp associated with the data point. InfluxDB uses time to store and query data in chronological order. Each point is tied to a specific time.

## Inserting Data into InfluxDB with Spring Boot
This section demonstrates how to insert data into InfluxDB using a Spring Boot application. We will use the InfluxDB Java client to interact with InfluxDB.

### Dependencies
Add the following dependencies to your Spring Boot project:

```xml
<dependency>
    <groupId>org.influxdb</groupId>
    <artifactId>influxdb-client-java</artifactId>
    <version>2.24.0</version>
</dependency>
<dependency>
    <groupId>org.influxdb</groupId>
    <artifactId>influxdb-spring</artifactId>
    <version>2.24.0</version>
</dependency>
```

### Configuration
Configure the InfluxDB connection in your application.properties file:

```properties
spring.influx.url=http://localhost:8086
spring.influx.token=mytoken
spring.influx.org=myorg
spring.influx.bucket=mybucket
```
    
### Writing Data
Use the InfluxDB Java client to write data to InfluxDB:

```java
package com.example.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfluxService {

    @Value("${influxdb.url}")
    private String influxUrl;

    @Value("${influxdb.token}")
    private String influxToken;

    @Value("${influxdb.org}")
    private String influxOrg;

    @Value("${influxdb.bucket}")
    private String influxBucket;

    private InfluxDBClient influxDBClient;

    public void init() {
        influxDBClient = InfluxDBClient
            .factory(influxUrl, influxToken.toCharArray(), influxOrg, influxBucket);
    }

    public void saveData(String measurement, String tagKey, String tagValue, String fieldKey, Object fieldValue) {
        Point point = Point.measurement(measurement)
            .addTag(tagKey, tagValue)
            .addField(fieldKey, fieldValue)
            .time(System.currentTimeMillis(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(point);
    }
}
```