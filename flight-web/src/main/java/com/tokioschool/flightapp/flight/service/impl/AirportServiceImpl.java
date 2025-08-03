package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.AirportDTO;
import com.tokioschool.flightapp.flight.repository.AirportDAO;
import com.tokioschool.flightapp.flight.service.AirportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

  private final AirportDAO airportDAO;
  private final ModelMapper modelMapper;

  @Override
  public List<AirportDTO> getAirports() {

    return airportDAO.findAll().stream()
        .map(airport -> modelMapper.map(airport, AirportDTO.class))
        .toList();
  }
}
