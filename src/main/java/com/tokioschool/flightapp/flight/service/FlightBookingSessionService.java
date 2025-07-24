package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.dto.FlightBookingSessionDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import java.util.Map;

public interface FlightBookingSessionService {

  void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO);

  FlightBookingDTO confirmFlightBookingSession(FlightBookingSessionDTO flightBookingSessionDTO);

  Map<Long, FlightDTO> getFlightsById(FlightBookingSessionDTO flightBookingSessionDTO);
}
