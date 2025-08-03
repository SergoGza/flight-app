package com.tokioschool.flightapp.flight.converter;

import com.tokioschool.flightapp.core.converter.ModelMapperConfiguration;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.ResourceDTO;
import com.tokioschool.flightapp.flight.domain.Airport;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import java.time.LocalDateTime;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class FlightConverterTest {

  private final ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

  @Test
  void given_user_whenConvertedToDTO_thenOk() {

    Flight flight =
        Flight.builder()
            .id(1L)
            .created(LocalDateTime.now())
            .number("number")
            .capacity(10)
            .departure(Airport.builder().acronym("BCN").build())
            .arrival(Airport.builder().acronym("IBZ").build())
            .departureTime(LocalDateTime.of(2022, 11, 2, 12, 30, 0))
            .status(FlightStatus.SCHEDULED)
            .occupancy(2)
            .image(
                FlightImage.builder()
                    .contentType("content-type")
                    .resourceID(UUID.randomUUID())
                    .filename("filename")
                    .size(10)
                    .build())
            .build();

    FlightDTO flightDTO = modelMapper.map(flight, FlightDTO.class);

    Assertions.assertThat(flightDTO)
        .returns(flight.getId(), FlightDTO::getId)
        .returns(flight.getCapacity(), FlightDTO::getCapacity)
        .returns(flight.getNumber(), FlightDTO::getNumber)
        .returns(flight.getDeparture().getAcronym(), FlightDTO::getDepartureAcronym)
        .returns(flight.getArrival().getAcronym(), FlightDTO::getArrivalAcronym)
        .returns(flight.getDepartureTime(), FlightDTO::getDepartureTime)
        .returns(flight.getStatus().name(), o -> o.getStatus().name())
        .satisfies(o -> Assertions.assertThat(o.getImage()).isNotNull());

    ResourceDTO resourceDTO = flightDTO.getImage();

    Assertions.assertThat(resourceDTO)
        .returns("content-type", ResourceDTO::getContentType)
        .returns("filename", ResourceDTO::getFilename)
        .returns(10, ResourceDTO::getSize)
        .satisfies(o -> Assertions.assertThat(o.getResourceId()).isNotNull());
  }
}
