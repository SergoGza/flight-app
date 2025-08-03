package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.FlightMvcDTO;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlightService {

  List<FlightDTO> getFlights();

  FlightDTO getFlight(Long flightId);

  @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
  FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

  @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
  FlightDTO editFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

    Map<Long, FlightDTO> getFlightsById(Set<Long> flightsIds);
}
