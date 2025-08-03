package com.tokioschool.flightapp.flight.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry
        .addMapping("/flight/**")
        .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
        .allowedOrigins("http://localhost:8080");

    registry
        .addMapping("/login")
        .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
        .allowedOrigins("http://localhost:8080");
  }
}
