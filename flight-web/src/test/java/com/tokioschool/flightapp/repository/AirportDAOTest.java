package com.tokioschool.flightapp.repository;

import com.tokioschool.flightapp.flight.domain.Airport;
import java.util.List;
import java.util.Optional;

import com.tokioschool.flightapp.flight.repository.AirportDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto:create-drop"
    })
class AirportDAOTest {

  @Autowired private AirportDAO airportDAO;

  @BeforeEach
  void beforeEach() {

    Airport bcn = Airport.builder().acronym("BCN").name("Barcelona").country("Spain").build();

    Airport gla =
        Airport.builder().acronym("GLA").name("Glasgow").country("United Kingdom").build();

    airportDAO.save(bcn);
    airportDAO.save(gla);
  }

  @Test
  void givenAirports_whenFindAll_thenReturnOk() {

    List<Airport> airports = airportDAO.findAll();

    Assertions.assertEquals(2, airports.size());
  }

  @Test
  void givenAirports_whenFindBcnAirport_thenReturnOk() {

    Optional<Airport> maybeBcn = airportDAO.findByAcronym("BCN");
    Assertions.assertTrue(maybeBcn.isPresent());
    Assertions.assertNotNull(maybeBcn.get().getAcronym());
  }
}
