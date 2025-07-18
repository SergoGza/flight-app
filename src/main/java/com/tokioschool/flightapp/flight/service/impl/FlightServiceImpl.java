package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.FlightMvcDTO;
import com.tokioschool.flightapp.flight.domain.Airport;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import com.tokioschool.flightapp.flight.repository.AirportDAO;
import com.tokioschool.flightapp.flight.repository.FlightDAO;
import com.tokioschool.flightapp.flight.service.FlightImageService;
import com.tokioschool.flightapp.flight.service.FlightService;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

  private final FlightDAO flightDAO;
  private final ModelMapper modelMapper;
  private final AirportDAO airportDAO;
  private final FlightImageService flightImageService;

  @Override
  public List<FlightDTO> getFlights() {

    return flightDAO.findAll().stream()
        .map(flight -> modelMapper.map(flight, FlightDTO.class))
        .toList();
  }

  @Override
  public FlightDTO getFlight(Long flightId) {
    return flightDAO
        .findById(flightId)
        .map(flight -> modelMapper.map(flight, FlightDTO.class))
        .orElseThrow(
            () -> new IllegalArgumentException("Flight with id:%s not found".formatted(flightId)));
  }

  @Override
  @Transactional
  public FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile) {

    Flight flight = createOrEdit(new Flight(), flightMvcDTO, multipartFile);
    return modelMapper.map(flight, FlightDTO.class);
  }

  @Override
  @Transactional
  public FlightDTO editFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile) {

    Flight flight =
        flightDAO
            .findById(flightMvcDTO.getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Flight with id:%s not found".formatted(flightMvcDTO.getId())));

    flight = createOrEdit(flight, flightMvcDTO, multipartFile);
    return modelMapper.map(flight, FlightDTO.class);
  }

  protected Flight createOrEdit(
      Flight flight, FlightMvcDTO flightMvcDTO, MultipartFile multipartFile) {

    Airport departure = getAirport(flightMvcDTO.getDeparture());
    Airport arrival = getAirport(flightMvcDTO.getArrival());

    FlightImage flightImage = flight.getImage();
    if (multipartFile.isEmpty()) {
      flightImage = flightImageService.saveImage(multipartFile);
    }

    flight.setCapacity(flightMvcDTO.getCapacity());
    flight.setArrival(arrival);
    flight.setDeparture(departure);
    flight.setStatus(FlightStatus.valueOf(flightMvcDTO.getStatus()));
    flight.setNumber(flightMvcDTO.getNumber());
    flight.setDepartureTime(flightMvcDTO.getDayTime());
    flight.setImage(flightImage);

    return flightDAO.save(flight);
  }

  protected Airport getAirport(String acronym) {
    return airportDAO
        .findByAcronym(acronym)
        .orElseThrow(
            () -> new IllegalArgumentException("Airport with acronym:%s".formatted(acronym)));
  }
}
