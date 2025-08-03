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


      @GetMapping("/my-error")
    public String getError() {
          throw new RuntimeException("This an error");
      }

}
