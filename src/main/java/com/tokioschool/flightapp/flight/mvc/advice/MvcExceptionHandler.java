package com.tokioschool.flightapp.flight.mvc.advice;

import com.tokioschool.flightapp.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(annotations = Controller.class)
public class MvcExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ModelAndView handleException(HttpServletRequest httpServletRequest, Exception exception) {

    ErrorDTO errorDTO =
        ErrorDTO.builder()
            .exception(ExceptionUtils.getMessage(exception))
            .url(httpServletRequest.getRequestURL().toString())
            .build();

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("error", errorDTO);
    modelAndView.setViewName("flight/error");
    return modelAndView;
  }
}
