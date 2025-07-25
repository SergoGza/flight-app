package com.tokioschool.flightapp.flight.mvc.controller;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.dto.FlightBookingSessionDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.flight.service.FlightBookingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes(names = {"flightBookingSessionDTO"})
public class FlightBookingMvcController {

  private final FlightBookingSessionService flightBookingSessionService;

  @ModelAttribute(name = "flightBookingSessionDTO")
  public FlightBookingSessionDTO flightBookingSessionDTO() {
    return new FlightBookingSessionDTO();
  }

  @GetMapping("/flight/bookings-add/{flightId}")
  public RedirectView addBooking(
      @PathVariable("flightId") Long flightId,
      @ModelAttribute(value = "flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO) {

    flightBookingSessionService.addFlightId(flightId, flightBookingSessionDTO);
    return new RedirectView("/flight/bookings");
  }

  @GetMapping("/flight/bookings")
  public ModelAndView getBookings(
      @ModelAttribute(value = "flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO) {
    Map<Long, FlightDTO> flightMap =
        flightBookingSessionService.getFlightsById(flightBookingSessionDTO);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("flightBookingSession", flightBookingSessionDTO);
    modelAndView.addObject("flightMap", flightMap);
    modelAndView.setViewName("/flight/bookings/bookings-list");
    return modelAndView;
  }

  @PostMapping("/flight/bookings-confirm")
  public RedirectView confirmBookings(
      @ModelAttribute(value = "flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO,
      SessionStatus sessionStatus) {

    FlightBookingDTO flightBookingDTO = flightBookingSessionService.confirmFlightBookingSession(flightBookingSessionDTO);

    sessionStatus.setComplete();

    return new RedirectView("/flight/bookings-confirm/%s".formatted(flightBookingDTO.getLocator()));
  }

  @GetMapping("/flight/bookings-confirm/{bookingLocator}")
  public ModelAndView confirmedBooking(@PathVariable("bookingLocator") String bookingLocator) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("locator", bookingLocator);
    modelAndView.setViewName("/flight/bookings/bookings-confirm");
    return modelAndView;
  }
}
