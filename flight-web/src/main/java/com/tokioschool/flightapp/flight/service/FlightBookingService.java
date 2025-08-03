package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightBookingDTO;

public interface FlightBookingService {

    FlightBookingDTO bookFlight(Long flightId, String userId);

}
