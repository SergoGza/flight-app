package com.tokioschool.flightapp.flight.mvc.controller;

import com.tokioschool.flightapp.dto.FlightBookingDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.domain.FlightBooking;
import com.tokioschool.flightapp.flight.domain.User;
import com.tokioschool.flightapp.flight.repository.FlightBookingDAO;
import com.tokioschool.flightapp.flight.repository.UserDAO;
import com.tokioschool.flightapp.flight.service.FlightBookingService;
import com.tokioschool.flightapp.flight.service.FlightService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FlightBookingMvcController {

  private final FlightBookingService flightBookingService;
  private final FlightService flightService;
  private final FlightBookingDAO flightBookingDAO;
  private final UserDAO userDAO;

  @GetMapping("/flight/bookings")
  public ModelAndView getBookings() {
    List<FlightBooking> bookings = flightBookingDAO.findAll();

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("/flight/bookings/bookings-list");
    modelAndView.addObject("bookings", bookings);
    return modelAndView;
  }

  @GetMapping("/flight/bookings/{bookingLocator}")
  public ModelAndView getBookingByLocator(
      @PathVariable(name = "bookingLocator") UUID bookingLocator) {

    Optional<FlightBooking> maybeFlightBooking =
        flightBookingDAO.findFlightBookingByLocator(bookingLocator);

    ModelAndView modelAndView = new ModelAndView();

    maybeFlightBooking.ifPresentOrElse(
        flightBooking -> {
          modelAndView.setViewName("/flight/bookings/booking-detail");
          modelAndView.addObject("booking", flightBooking);
        },
        () -> {
          modelAndView.setViewName("error/404");
          modelAndView.addObject(
              "errorMessage", "Reserva no encontrada con localizador: " + bookingLocator);
        });

    return modelAndView;
  }

  @GetMapping("/flight/bookings/new/{flightId}")
  public ModelAndView createBookingForm(
      @PathVariable(name = "flightId") Long flightId, Model model) {

    Optional<FlightDTO> maybeFlightDTO =
        Optional.ofNullable(flightId).map(flightService::getFlight);

    ModelAndView modelAndView = new ModelAndView();

    maybeFlightDTO.ifPresentOrElse(
        flightDTO -> {
          List<User> users = userDAO.findAll();

          modelAndView.setViewName("/flight/bookings/booking-new");
          modelAndView.addObject("flight", flightDTO);
          modelAndView.addObject("users", users);
        },
        () -> {
          modelAndView.setViewName("error/404");
          modelAndView.addObject("errorMessage", "Vuelo no encontrado con ID: " + flightId);
        });

    return modelAndView;
  }

  @PostMapping("/flight/bookings/new")
  public Object createBooking(
      @RequestParam(name = "flightId") Long flightId,
      @RequestParam(name = "userId") String userId,
      RedirectAttributes redirectAttributes) {

    try {
      FlightBookingDTO flightBookingDTO = flightBookingService.bookFlight(flightId, userId);

      redirectAttributes.addFlashAttribute(
          "successMessage",
          "Reserva creada exitosamente. Localizador: " + flightBookingDTO.getLocator());

      return new RedirectView("/flight/bookings");

    } catch (IllegalArgumentException e) {
      log.error("Error al crear la reserva - datos inválidos", e);
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return new RedirectView("/flight/bookings/new/" + flightId);

    } catch (IllegalStateException e) {
      log.error("Error al crear la reserva - estado inválido", e);
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return new RedirectView("/flight/flights");
    }
  }

  @GetMapping("/flight/bookings/by-flight/{flightId}")
  public ModelAndView getBookingsByFlight(@PathVariable(name = "flightId") Long flightId) {

    List<FlightBooking> bookings = flightBookingDAO.getFlightBookingsByFlightId(flightId);
    Optional<FlightDTO> maybeFlightDTO =
        Optional.ofNullable(flightId).map(flightService::getFlight);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("/flight/bookings/bookings-by-flight");
    modelAndView.addObject("bookings", bookings);
    modelAndView.addObject("flight", maybeFlightDTO.orElse(null));

    return modelAndView;
  }

  @PostMapping("/flight/bookings/cancel/{bookingId}")
  public RedirectView cancelBooking(
      @PathVariable(name = "bookingId") Long bookingId, RedirectAttributes redirectAttributes) {

    Optional<FlightBooking> maybeFlightBooking = flightBookingDAO.findById(bookingId);

    maybeFlightBooking.ifPresentOrElse(
        flightBooking -> {
          // Actualizar la ocupación del vuelo
          Flight flight = flightBooking.getFlight();
          flight.setOccupancy(flight.getOccupancy() - 1);
          flight.getBookings().remove(flightBooking);

          flightBookingDAO.delete(flightBooking);

          redirectAttributes.addFlashAttribute("successMessage", "Reserva cancelada exitosamente");
        },
        () -> {
          redirectAttributes.addFlashAttribute(
              "errorMessage", "No se encontró la reserva a cancelar");
        });

    return new RedirectView("/flight/bookings");
  }
}
