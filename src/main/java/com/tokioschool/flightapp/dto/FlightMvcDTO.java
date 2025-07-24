package com.tokioschool.flightapp.dto;

import java.time.LocalDateTime;

import com.tokioschool.flightapp.core.validator.EnumValid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightMvcDTO {

  private Long id;

  @NotBlank
  private String number;

  @NotBlank
  private String departure;

  @NotBlank
  private String arrival;

  @NotNull
  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
  private LocalDateTime dayTime;

  @EnumValid(target = FlightDTO.Status.class, required = true, message = "{validation.flight.status.invalid}")
  private String status;

  @NotNull(message = "no debe ser nulo")
  @Positive(message = "debe ser mayor que 0")
  @Digits(fraction = 0, integer = 3)
  private Integer capacity;
}