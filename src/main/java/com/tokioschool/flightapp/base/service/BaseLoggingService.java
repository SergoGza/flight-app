package com.tokioschool.flightapp.base.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile(value = {"dev"})
public class BaseLoggingService {

  private final Environment environment;

  @Value("${application.custom:'no-value'}")
  private String custom;

  @PostConstruct
  void postConstruct() {

    log.info(
        "BaseLogginService, {} custom {} profiles: [{}]",
        "info",
        custom,
        String.join("", environment.getActiveProfiles()));

    log.trace("BaseLoggingService, {}", "trace");
    log.debug("BaseLoggingService, {}", "debug");
    log.info("BaseLoggingService, {}", "info");
    log.warn("BaseLoggingService, {}", "warn");
  }
}
