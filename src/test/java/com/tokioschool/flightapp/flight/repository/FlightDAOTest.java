package com.tokioschool.flightapp.flight.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.tokioschool.flightapp.flight.domain.Airport;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import com.tokioschool.flightapp.flight.projection.FlightCancelledCounterByAirport;
import com.tokioschool.flightapp.flight.projection.FlightCounterByAirport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@DataJpaTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto:create-drop"
    })
class FlightDAOTest {

  @Autowired private AirportDAO airportDAO;
  @Autowired private FlightDAO flightDAO;
  @Autowired private FlightImageDAO flightImageDAO;

  @BeforeEach
  void beforeEach() {

    Airport bcn = Airport.builder().acronym("BCN").name("Barcelona").country("Spain").build();

    Airport gla =
        Airport.builder().acronym("GLA").name("Glasgow").country("United Kingdom").build();

    airportDAO.save(bcn);
    airportDAO.save(gla);

    Flight flight1 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(bcn)
            .arrival(gla)
            .number("EJU3037")
            .departureTime(LocalDateTime.now().plusSeconds(120))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    FlightImage flightImage =
        FlightImage.builder()
            .flight(flight1)
            .resourceID(UUID.randomUUID())
            .contentType("image/jpeg")
            .size(20)
            .build();

    flight1.setImage(flightImage);

    Flight flight2 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(bcn)
            .arrival(gla)
            .number("KLM3037")
            .departureTime(LocalDateTime.now().minusSeconds(60))
            .status(FlightStatus.CANCELLED)
            .bookings(new HashSet<>())
            .build();

    Flight flight3 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(bcn)
            .arrival(gla)
            .number("EJU3037")
            .departureTime(LocalDateTime.now().minusSeconds(60))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    flightDAO.saveAll(List.of(flight1, flight2, flight3));
  }


  @Test
  void givenFlights_whenFindByDeparture_thenOk() {
    List<Flight> flightsBcn = flightDAO.findByDepartureAcronym("BCN");

    assertEquals(3, flightsBcn.size());
    List<Flight> flightsGla = flightDAO.findByDepartureAcronym("GLA");
    assertEquals(0,flightsGla.size());
  }

  @Test
  void givenFlights_whenFindNextFlights_thenOk() {

    List<Flight> flights = flightDAO.findByDepartureTimeIsAfterAndStatusIs(
            LocalDateTime.now(), FlightStatus.SCHEDULED
    );

    assertEquals(1, flights.size());
    assertNotNull(flights.get(0).getImage());

  }

  @Test
  void givenFlights_whenFindLikeNumber_thenOk(){
    List<Flight> flightsLike = flightDAO.findByNumberLike("%EJU%");
    assertEquals(2, flightsLike.size());
    List<Flight> flightsContains = flightDAO.findByNumberContains("EJU");
    assertEquals(2, flightsContains.size());

  }

  @Test
  void givenFlights_whenGetAllPagesSorted_thenOk(){

    PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "departureTime"));

    Page<Flight> page1 = flightDAO.findAll(pageRequest);

    assertEquals(2, page1.getTotalPages());
    assertEquals(3, page1.getTotalElements());
    assertEquals(2, page1.getNumberOfElements());
    assertEquals("EJU3037", page1.getContent().get(0).getNumber());

    pageRequest = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "departureTime"));
    Page<Flight> page2 = flightDAO.findAll(pageRequest);

    assertEquals(1, page2.getNumberOfElements());
    assertEquals("KLM3037", page2.getContent().get(0).getNumber());

  }

  @Test
  void givenFlights_whenFindDepartureCounters_thenOk(){

    List <FlightCounterByAirport> flightCounterByAirport = flightDAO.getFlightCCounterByDepartureAirport();

    Assertions.assertThat(flightCounterByAirport).hasSize(1);
    Assertions.assertThat(flightCounterByAirport.get(0))
            .returns(3L, FlightCounterByAirport::getCounter)
            .returns("BCN", FlightCounterByAirport::getAcronym);

  }

  @Test
  void givenFlights_whenFindCancelledCounters_thenOk(){

    List<FlightCancelledCounterByAirport> flightCancelledCounterByAirport = flightDAO.getFlightStatusCounterByAirport(FlightStatus.CANCELLED);
    Assertions.assertThat(flightCancelledCounterByAirport).hasSize(1);
    Assertions.assertThat(flightCancelledCounterByAirport.get(0))
            .returns(1L, FlightCancelledCounterByAirport::counter)
            .returns("BCN", FlightCancelledCounterByAirport::acronym);
  }

  @Test
  void givenFlight_whenUpdatingNumber_thenOk() {
    Flight flight = flightDAO.findByNumberLike("EJU3037").get(0);
    flightDAO.updateFlightNumber(flight.getId(),"EBU3037");

    List<Flight> flights = flightDAO.findByNumberLike("EBU3037");

    Assertions.assertThat(flights).hasSize(1);
    Assertions.assertThat(flights.get(0).getNumber()).isEqualTo("EBU3037");
  }
}
