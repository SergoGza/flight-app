package com.tokioschool.flightapp.flight.mvc.controller;

import com.tokioschool.flightapp.dto.AirportDTO;
import com.tokioschool.flightapp.dto.FlightDTO;
import com.tokioschool.flightapp.dto.FlightMvcDTO;
import com.tokioschool.flightapp.dto.ResourceDTO;
import com.tokioschool.flightapp.flight.service.AirportService;
import com.tokioschool.flightapp.flight.service.FlightService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tokioschool.flightapp.flight.validator.FlightMvcDTOValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FlightMvcController {

  private final FlightService flightService;
  private final AirportService airportService;
  private final FlightMvcDTOValidator flightMvcDTOValidator;

  @InitBinder
  private void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.setValidator(flightMvcDTOValidator);
  }

  @GetMapping("flight/flights")
  public ModelAndView getFlights() {

    List<FlightDTO> flights = flightService.getFlights();

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("/flight/flights/flights-list");
    modelAndView.addObject("flights", flights);
    return modelAndView;
  }

  @GetMapping({"/flight/flights-edit", "/flight/flights-edit/{flightId}"})
  public ModelAndView createOrEditFlight(
          @PathVariable(name = "flightId", required = false) Long flightId, Model model) {

    Optional<FlightDTO> maybeFlightDTO =
            Optional.ofNullable(flightId).map(flightService::getFlight);

    FlightMvcDTO flightMvcDTO =
            maybeFlightDTO
                    .map(
                            flightDTO ->
                                    FlightMvcDTO.builder()
                                            .id(flightDTO.getId())
                                            .number(flightDTO.getNumber())
                                            .capacity(flightDTO.getCapacity())
                                            .arrival(flightDTO.getArrivalAcronym())
                                            .departure(flightDTO.getDepartureAcronym())
                                            .status(flightDTO.getStatus().name())
                                            .dayTime(flightDTO.getDepartureTime())
                                            .build())
                    .orElseGet(FlightMvcDTO::new);

    ModelAndView modelAndView =
            populateCreateOrEditFlightModel(flightMvcDTO, maybeFlightDTO.orElse(null), model);
    modelAndView.setViewName("flight/flights/flights-edit");

    return modelAndView;
  }

  @PostMapping({"/flight/flights-edit", "/flight/flights-edit/", "/flight/flights-edit/{flightId}"})
  public Object createOrEditFlightPost(
          @Valid @ModelAttribute("flight") FlightMvcDTO flightMvcDTO,
          BindingResult bindingResult,
          @RequestParam("image") MultipartFile multipartFile,
          @PathVariable(name = "flightId", required = false) Long flightId,
          Model model) {

    // Log para debugging
    log.debug("Received flight DTO: {}", flightMvcDTO);
    log.debug("Binding errors: {}", bindingResult.getAllErrors());

    // Si hay errores de validación, volver a mostrar el formulario con los errores
    if (bindingResult.hasErrors()) {
      FlightDTO flightDTO = null;
      if (flightMvcDTO.getId() != null) {
        try {
          flightDTO = flightService.getFlight(flightMvcDTO.getId());
        } catch (Exception e) {
          log.error("Error al obtener el vuelo con id: {}", flightMvcDTO.getId(), e);
        }
      }

      ModelAndView modelAndView = populateCreateOrEditFlightModel(flightMvcDTO, flightDTO, model);
      modelAndView.setViewName("flight/flights/flights-edit");
      return modelAndView;
    }

    // Si no hay errores de validación, procesar el formulario
    try {
      Optional.ofNullable(flightMvcDTO.getId())
              .map(o -> flightService.editFlight(flightMvcDTO, multipartFile))
              .orElseGet(() -> flightService.createFlight(flightMvcDTO, multipartFile));

      return new RedirectView("/flight/flights");
    } catch (Exception e) {
      log.error("Error al procesar el vuelo", e);

      // Si hay un error de negocio, añadir el error al BindingResult
      bindingResult.reject("error.global", e.getMessage());

      FlightDTO flightDTO = null;
      if (flightMvcDTO.getId() != null) {
        try {
          flightDTO = flightService.getFlight(flightMvcDTO.getId());
        } catch (Exception ex) {
          log.error("Error al obtener el vuelo con id: {}", flightMvcDTO.getId(), ex);
        }
      }

      ModelAndView modelAndView = populateCreateOrEditFlightModel(flightMvcDTO, flightDTO, model);
      modelAndView.setViewName("flight/flights/flights-edit");
      return modelAndView;
    }
  }

  private ModelAndView populateCreateOrEditFlightModel(
          FlightMvcDTO flightMvcDTO, @Nullable FlightDTO flightDTO, Model model) {

    List<AirportDTO> airports = airportService.getAirports();

    UUID imageId = Optional.ofNullable(flightDTO)
            .map(FlightDTO::getImage)
            .map(ResourceDTO::getResourceId)
            .orElse(null);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addAllObjects(model.asMap());
    modelAndView.addObject("flight", flightMvcDTO);
    modelAndView.addObject("airports", airports);
    modelAndView.addObject("flightImageResourceId", imageId);

    return modelAndView;
  }
}