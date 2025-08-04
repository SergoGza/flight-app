package com.tokioschool.flightapp.store.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class StoreApiSecurityConfiguration {


  @Bean
  public SecurityFilterChain configureApiConfiguration(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .securityMatcher("/store/api")
        .authorizeHttpRequests(
            authorizeRequest ->
                authorizeRequest
                    .requestMatchers("/store/api/auth")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/store/api/resources")
                    .hasAuthority("write-resource")
                    .requestMatchers(HttpMethod.DELETE, "/store/api/resources/**")
                    .hasAuthority("write-resource")
                    .requestMatchers(HttpMethod.GET, "/store/api/resources/**")
                    .hasAnyAuthority("write-resource", "read-resource")
                    .requestMatchers("/store/api")
                    .authenticated())
        .sessionManagement(
            httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }
}
