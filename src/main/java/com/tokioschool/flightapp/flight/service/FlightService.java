package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.FlightMvcDTO;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FlightService {

  List<FlightDTO> getFlights();

  FlightDTO getFlight(Long flightId);

  FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

  FlightDTO editFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);
}
