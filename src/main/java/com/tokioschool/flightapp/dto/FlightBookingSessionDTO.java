package com.tokioschool.flightapp.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightBookingSessionDTO {

  private Long currentFlightId;
  private Set<Long> discaredFlightIds = new HashSet<>();
}
