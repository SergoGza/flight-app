package com.tokioschool.flightapp.flight.converter;

import com.tokioschool.flightapp.core.converter.ModelMapperConfiguration;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.flight.domain.Airport;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightStatus;
import java.time.LocalDateTime;
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
            .image(null)
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
        .satisfies(o -> Assertions.assertThat(o.getImage()).isNull());
  }
}
