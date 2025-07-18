package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import com.tokioschool.flightapp.flight.projection.FlightCancelledCounterByAirport;
import com.tokioschool.flightapp.flight.projection.FlightCounterByAirport;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDAO extends JpaRepository<Flight, Long> {

  List<Flight> findByDepartureAcronym(String departureAcronym);

  List<Flight> findByDepartureTimeIsAfterAndStatusIs(
      LocalDateTime departureTimeFrom, FlightStatus flightStatus);

  List<Flight> findByNumberLike(String number);

  List<Flight> findByNumberContains(String number);

  @Query(
      """
          SELECT f.departure.acronym AS acronym, COUNT (1) AS counter
          FROM Flight f
          GROUP BY f.departure.acronym
          """)
  List<FlightCounterByAirport> getFlightCCounterByDepartureAirport();

  @Query(
value = """
SELECT airports.id as acronym, COUNT(1) as counter
FROM flights
JOIN airports ON flights.airport_arrival_id = airports.id
GROUP BY airports.id
""", nativeQuery = true)
  List<FlightCounterByAirport> getFlightCCounterByArrivalAirport();

  @Query(
"""
SELECT new com.tokioschool.flightapp.flight.projection.FlightCancelledCounterByAirport(f.departure.acronym, COUNT (1))
FROM Flight f
WHERE f.status = :status
GROUP BY f.departure.acronym
""")
  List<FlightCancelledCounterByAirport> getFlightStatusCounterByAirport(
      @Param("status") FlightStatus status);

  @Query(
"""
UPDATE Flight f
SET f.number = :number
where f.id = :id
""")
  @Modifying (clearAutomatically = true)
  int updateFlightNumber(@Param("id") Long flightId, @Param("number") String number);
}
