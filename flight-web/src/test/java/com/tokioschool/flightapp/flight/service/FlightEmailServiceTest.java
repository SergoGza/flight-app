package com.tokioschool.flightapp.flight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tokioschool.flightapp.dto.FlightImageResourceDTO;
import com.tokioschool.flightapp.email.dto.EmailDTO;
import com.tokioschool.flightapp.email.service.EmailService;
import com.tokioschool.flightapp.flight.domain.Airport;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import com.tokioschool.flightapp.flight.repository.AirportDAO;
import com.tokioschool.flightapp.flight.repository.FlightDAO;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto:create-drop"
    })
class FlightEmailServiceTest {

  @MockBean private FlightImageService flightImageService;

  @SpyBean private EmailService emailService;

  @Autowired private FlightEmailService flightEmailService;
  @Autowired private FlightDAO flightDAO;
  @Autowired private AirportDAO airportDAO;

  private Flight flight;

  @BeforeEach
  void beforeEach() {

    Airport bcn =
        Airport.builder().acronym("BCN").name("Barcelona airport").country("Spain").build();
    Airport gla =
        Airport.builder().acronym("GLA").name("Glasgow airport").country("United Kingdom").build();

    airportDAO.saveAll(List.of(bcn, gla));

    flight =
        Flight.builder()
            .occupancy(80)
            .capacity(90)
            .departure(bcn)
            .arrival(gla)
            .number("EJU3837")
            .departureTime(LocalDateTime.now().plusDays(30))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    flight.setImage(FlightImage.builder().resourceID(UUID.randomUUID()).flight(flight).build());

    flightDAO.save(flight);
  }

  @Test
  void givenFlight_whenSendingEmail_thenOK() throws Exception {

    InputStream flightTestResource = this.getClass().getResourceAsStream("/flight-test.jpg");

    FlightImageResourceDTO flightImageResourceDTO =
        FlightImageResourceDTO.builder()
            .contentType("image/jpeg")
            .filename("flight-test.jpg")
            .content(flightTestResource.readAllBytes())
            .build();

    Mockito.when(flightImageService.getImage(flight.getImage().getResourceID()))
        .thenReturn(flightImageResourceDTO);

    Mockito.doNothing().when(emailService).sendEmail(Mockito.any());

    flightEmailService.sendFlightEmail(flight.getId(), "sergiogza0@gmail.com");

    ArgumentCaptor<EmailDTO> emailDTOArgumentCaptor = ArgumentCaptor.forClass(EmailDTO.class);

    Mockito.verify(emailService, Mockito.times(1)).sendEmail(emailDTOArgumentCaptor.capture());

    assertThat(emailDTOArgumentCaptor.getValue())
        .returns("sergiogza0@gmail.com", EmailDTO::getTo)
        .satisfies(emailDTO -> assertThat(emailDTO.getAttachments()).hasSize(1));
  }
}
