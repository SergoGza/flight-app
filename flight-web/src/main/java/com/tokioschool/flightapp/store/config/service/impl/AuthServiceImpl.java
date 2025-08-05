package com.tokioschool.flightapp.store.config.service.impl;

import com.tokioschool.flightapp.store.config.StoreConfigurationProperties;
import com.tokioschool.flightapp.store.config.service.AuthService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final StoreConfigurationProperties storeConfigurationProperties;
  private final RestClient restClient = RestClient.builder().build();

  private String accessToken;
  private long expiresAt;

  @Nullable
  @Override
  public String getAccesToken() {

    if (System.currentTimeMillis() < expiresAt) {
      return accessToken;
    }

    Map<String, String> authRequest =
        Map.of(
            "username",
            storeConfigurationProperties.username(),
            "passowrd",
            storeConfigurationProperties.password());

    try {
      AuthResponseDTO authResponseDTO =
          restClient
              .post()
              .uri(storeConfigurationProperties.baseUrl() + "/store/api/auth")
              .contentType(MediaType.APPLICATION_JSON)
              .body(authRequest)
              .retrieve()
              .body(AuthResponseDTO.class);

      accessToken = authResponseDTO.accesToken;
      expiresAt = authResponseDTO.getExpiresAt();
    } catch (Exception e) {
      log.error("Error while getting access token", e);
    }

    return accessToken;
  }

  private record AuthResponseDTO(String accesToken, int expiresIn) {
    public long getExpiresAt() {
      return System.currentTimeMillis() + expiresIn * 1000L - 5 * 1000L;
    }
  }
}
