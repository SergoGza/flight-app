package com.tokioschool.flightapp.store.service.impl;

import com.tokioschool.flightapp.store.config.JwtConfigurationProperties;
import com.tokioschool.flightapp.store.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;

@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final JwtConfigurationProperties jwtConfigurationProperties;
  private final NimbusJwtEncoder nimbusJwtEncoder;

  @Override
  public Jwt generateToken(UserDetails userDetails) {

    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();

    JwtClaimsSet jwtClaimSet =
        JwtClaimsSet.builder()
            .subject(userDetails.getUsername())
            .expiresAt(Instant.now().plusMillis(jwtConfigurationProperties.duration().toMillis()))
            .claim(
                "authorities",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .build();

    JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimSet);

    return nimbusJwtEncoder.encode(jwtEncoderParameters);
  }
}
