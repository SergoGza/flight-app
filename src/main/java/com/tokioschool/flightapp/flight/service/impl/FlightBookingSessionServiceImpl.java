package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.dto.FlightBookingSessionDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.flight.service.FlightBookingService;
import com.tokioschool.flightapp.flight.service.FlightBookingSessionService;
import com.tokioschool.flightapp.flight.service.FlightService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightBookingSessionServiceImpl implements FlightBookingSessionService {

  private FlightService flightService;
  private FlightBookingService flightBookingService;

  @Override
  public void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO) {

    FlightDTO flightDTO = flightService.getFlight(flightId);

    Optional.ofNullable(flightBookingSessionDTO.getCurrentFlightId())
        .ifPresent(
            discaredFlightId ->
                flightBookingSessionDTO.getDiscaredFlightIds().add(discaredFlightId));

    flightBookingSessionDTO.setCurrentFlightId(flightDTO.getId());
    flightBookingSessionDTO.getDiscaredFlightIds().remove(flightDTO.getId());
  }

  @Override
  public FlightBookingDTO confirmFlightBookingSession(
      FlightBookingSessionDTO flightBookingSessionDTO) {

    FlightDTO flightDTO = flightService.getFlight(flightBookingSessionDTO.getCurrentFlightId());
    return flightBookingService.bookFlight(flightDTO.getId(), "0AYZF78722501");
  }

  @Override
  public Map<Long, FlightDTO> getFlightsById(FlightBookingSessionDTO flightBookingSessionDTO) {

    Set<Long> flightsIds = new HashSet<>(flightBookingSessionDTO.getDiscaredFlightIds());
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
