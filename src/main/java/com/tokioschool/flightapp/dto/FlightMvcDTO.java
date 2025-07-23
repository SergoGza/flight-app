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

  @NotBlank(message = "no debe estar vacío")
  private String number;

  @NotBlank(message = "no debe estar vacío")
  private String departure;

  @NotBlank(message = "no debe estar vacío")
  private String arrival;

  @NotNull(message = "no debe ser nulo")
  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
  private LocalDateTime dayTime;

  @EnumValid(target = FlightDTO.Status.class, required = true, message = "debe seleccionar un estado válido")
  private String status;

  @NotNull(message = "no debe ser nulo")
  @Positive(message = "debe ser mayor que 0")
  @Digits(fraction = 0, integer = 3, message = "debe ser un número entero de máximo 3 dígitos")
  private Integer capacity;
}