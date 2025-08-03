package com.tokioschool.flightapp.flight.validator;

import com.tokioschool.flightapp.dto.FlightMvcDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Component
@RequiredArgsConstructor
public class FlightMvcDTOValidator extends CustomValidatorBean {


  private final LocalValidatorFactoryBean localValidatorFactoryBean;

  @Override
  public void validate(Object target, Errors errors) {


    localValidatorFactoryBean.validate(target, errors);

    if  (errors.hasErrors() || !(target instanceof FlightMvcDTO flightMvcDTO)) {
      return;
    }
    if (flightMvcDTO.getDeparture().equals(flightMvcDTO.getArrival())) {
      errors.rejectValue(
          "arrival",
          "validation.flight.arrival_equals_departure",
          "arrival cannot be the same as departure");
    }
  }
}
