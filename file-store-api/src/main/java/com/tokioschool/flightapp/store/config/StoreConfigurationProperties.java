package com.tokioschool.flightapp.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.util.List;

@ConfigurationProperties(prefix = "application.store")
public record StoreConfigurationProperties(Path basePath, List<User> users) {

  public Path getPath(String file) {
    return Path.of(basePath.toString(), file);
  }

  public record User(String username, String password, List<String> authorities) {}
}
