package com.tokioschool.flightapp.store.service.impl;

import com.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;
import com.tokioschool.flightapp.store.service.AuthenticationService;
import com.tokioschool.flightapp.store.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Jwt jwt = jwtService.generateToken(userDetails);

    return AuthenticationResponseDTO.builder()
        .accesToken(jwt.getTokenValue())
        .expiresIn(ChronoUnit.SECONDS.between(Instant.now(), jwt.getExpiresAt()) + 1)
        .build();
  }

  @Override
  public AuthenticatedMeResponseDTO getAuthenticated() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return AuthenticatedMeResponseDTO.builder()
        .username(authentication.getName())
        .authorities(
            authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
        .build();
  }
}
