package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightBooking;
import com.tokioschool.flightapp.flight.domain.User;
import com.tokioschool.flightapp.flight.repository.FlightDAO;
import com.tokioschool.flightapp.flight.repository.UserDAO;
import com.tokioschool.flightapp.flight.service.FlightBookingService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightBookingServiceImpl implements FlightBookingService {

  private final FlightDAO flightDAO;
  private final UserDAO userDAO;
  private final ModelMapper modelMapper;

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public FlightBookingDTO bookFlight(Long flightId, String userId) {

    Flight flight =
        flightDAO
            .findById(flightId)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Flight with id:%s not found".formatted(flightId)));

    User user =
        userDAO
            .findById(userId)
            .orElseThrow(
                () -> new IllegalArgumentException("User with id:%s not found".formatted(userId)));

    if (flight.getOccupancy() >= flight.getCapacity()) {
      throw new IllegalStateException("Flight with id:%s without free places".formatted(flightId));
    }

    FlightBooking flightBooking =
        FlightBooking.builder().user(user).flight(flight).locator(UUID.randomUUID()).build();
    flight.setOccupancy(flight.getOccupancy() + 1);
    flight.getBookings().add(flightBooking);

    flightDAO.save(flight);

    return modelMapper.map(flightBooking, FlightBookingDTO.class);
  }
}
