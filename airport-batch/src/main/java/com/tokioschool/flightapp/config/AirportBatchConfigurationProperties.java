package com.tokioschool.flightapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "application.batch")
public record AirportBatchConfigurationProperties(Path basePath) {
  public Path getAirportCsvPath() {
    return Path.of(basePath.toString(), "airports.csv");
  }
}
