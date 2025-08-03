package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.dto.FlightBookingSessionDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.UserDTO;
import com.tokioschool.flightapp.flight.service.FlightBookingService;
import com.tokioschool.flightapp.flight.service.FlightBookingSessionService;
import com.tokioschool.flightapp.flight.service.FlightService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.tokioschool.flightapp.flight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightBookingSessionServiceImpl implements FlightBookingSessionService {

  private final FlightService flightService;
  private final FlightBookingService flightBookingService;
  private final UserService userService;

  @Override
  public void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO) {

    FlightDTO flightDTO = flightService.getFlight(flightId);

    Optional.ofNullable(flightBookingSessionDTO.getCurrentFlightId())
        .ifPresent(
            discardedFlightId ->
                flightBookingSessionDTO.getDiscardedFlightIds().add(discardedFlightId));

    flightBookingSessionDTO.setCurrentFlightId(flightDTO.getId());
    flightBookingSessionDTO.getDiscardedFlightIds().remove(flightDTO.getId());
  }

  @Override
  public FlightBookingDTO confirmFlightBookingSession(
      FlightBookingSessionDTO flightBookingSessionDTO) {

    FlightDTO flightDTO = flightService.getFlight(flightBookingSessionDTO.getCurrentFlightId());

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    UserDTO userDTO = userService
            .findByEmail(username)
            .orElseThrow(
                    () -> new IllegalArgumentException("User with email:%s not found".formatted(username)));

    return flightBookingService.bookFlight(flightDTO.getId(), userDTO.getId());
  }

  @Override
  public Map<Long, FlightDTO> getFlightsById(FlightBookingSessionDTO flightBookingSessionDTO) {

    Set<Long> flightsIds = new HashSet<>(flightBookingSessionDTO.getDiscardedFlightIds());
    Optional.ofNullable(flightBookingSessionDTO.getCurrentFlightId()).ifPresent(flightsIds::add);

    Map<Long, FlightDTO> flightMap = flightService.getFlightsById(flightsIds);

    for (Long flightId : flightsIds) {
      if (!flightMap.containsKey(flightId)) {
        throw new IllegalArgumentException("Flight with id:%s not found".formatted(flightId));
      }
    }

    return flightMap;
  }
}
