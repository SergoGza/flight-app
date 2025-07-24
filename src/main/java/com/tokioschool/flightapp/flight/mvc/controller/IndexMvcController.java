package com.tokioschool.flightapp.flight.mvc.controller;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flight")
public class IndexMvcController {

      @GetMapping
      public String getIndex() {
          return "flight/index";
      }

//  @Autowired private MessageSource messageSource;
//
//  @GetMapping
//  public String getIndex(Locale locale) {
//    // Pon un breakpoint aquí
//    String message = messageSource.getMessage("flight.nav.flights", null, locale);
//    System.out.println("Locale actual: " + locale);
//    System.out.println("Mensaje: " + message);
//    return "flight/index";
//  }
}
