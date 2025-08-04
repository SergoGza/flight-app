package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.flight.repository.AirportDAO;
import com.tokioschool.flightapp.flight.repository.FlightDAO;
import com.tokioschool.flightapp.flight.service.impl.FlightServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@EnableMethodSecurity
@ContextConfiguration(
    classes = {FlightServiceSecurityTest.MockConfiguration.class, FlightServiceImpl.class})
class FlightServiceSecurityTest {

  @SpyBean private FlightService flightService;

  @Test
  void givenNoUser_whenCreateFlight_thenAuthenticationMissingException() {
    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
        .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
  }

  @Test
  @WithAnonymousUser
  void givenAnonymous_whenCreateFlight_thenDeniedException() {
    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @WithMockUser(
      username = "username",
      authorities = {"ROLE_USER"})
  void givenUser_whenCreateFlight_thenDeniexException() {
    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @WithMockUser(
          username = "username",
          authorities = {"ROLE_ADMIN"})
  void givenAdmin_whenCreateFlight_thenOk() {

    Mockito.doReturn(null).when(flightService).createFlight(Mockito.any(), Mockito.any());

    FlightDTO flightDTO = flightService.createFlight(null, null);
    Assertions.assertThat(flightDTO).isNull();

  }

  @Configuration
  public static class MockConfiguration {
    @Bean
    FlightDAO flightDAO() {
      return Mockito.mock(FlightDAO.class);
    }

    @Bean
    ModelMapper modelMapper() {
      return Mockito.mock(ModelMapper.class);
    }

    @Bean
    AirportDAO airportDAO() {
      return Mockito.mock(AirportDAO.class);
    }

    @Bean
    FlightImageService flightImageService() {
      return Mockito.mock(FlightImageService.class);
    }
  }
}
