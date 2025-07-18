package com.tokioschool.flightapp.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightMvcDTO {

  private Long id;
  private String number;
  private String departure;
  private String arrival;
  private LocalDateTime dayTime;
  private String status;
  private Integer capacity;
}
