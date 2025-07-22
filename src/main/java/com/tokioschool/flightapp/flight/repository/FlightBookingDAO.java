package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.FlightBooking;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBookingDAO extends JpaRepository<FlightBooking, Long> {

  List<FlightBooking> getFlightBookingsByFlightId(Long flightId);

  Optional<FlightBooking> findFlightBookingByLocator(UUID bookingLocator);
}
