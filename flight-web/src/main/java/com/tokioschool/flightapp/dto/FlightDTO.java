package com.tokioschool.flightapp.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightDTO {

  private Long id;
  private String number;
  private String departureAcronym;
  private String arrivalAcronym;
  private LocalDateTime departureTime;
  private Status status;
  private Integer capacity;
  private Integer occupancy;
  private ResourceDTO image;

  public enum Status {
    SCHEDULED,
    CANCELLED;
  }
}
