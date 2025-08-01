package com.tokioschool.flightapp.flight.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MvcSecurityConfiguration {

  @Bean
  @Order(1)
  public SecurityFilterChain configureMvcSecurity(final HttpSecurity httpSecurity)
      throws Exception {

    return httpSecurity
        .securityMatcher("/flight/**", "/login", "/logout")
        .authorizeHttpRequests(
            authorizeRequest ->
                authorizeRequest
                    .requestMatchers("/login", "/logout", "/flight/my-error")
                    .permitAll()
                        .requestMatchers("/flight/flights-edit", "/flight/flights-edit/**")
                        .hasAuthority("ADMIN")
                    .requestMatchers("/flight/**")
                    .authenticated())
            .formLogin(
                    loginCustomizer -> loginCustomizer.loginPage("/login").defaultSuccessUrl("/flight")
            )
            .logout(logoutCustomizer -> logoutCustomizer.logoutUrl("/logout").logoutSuccessUrl("/login"))
            .csrf(Customizer.withDefaults())
            .cors(Customizer.withDefaults())
        .build();
  }
}
