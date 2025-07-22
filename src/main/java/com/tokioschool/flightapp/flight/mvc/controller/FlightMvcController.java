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

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class FlightMvcController {

  private final FlightService flightService;
  private final AirportService airportService;

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
  public RedirectView createOrEditFlightPost(
      @ModelAttribute("flight") FlightMvcDTO flightMvcDTO,
      @RequestParam("image") MultipartFile multipartFile,
      @PathVariable(name = "flightId", required = false) Long flightId,
      Model model) {
    Optional.ofNullable(flightMvcDTO.getId())
        .map(o -> flightService.editFlight(flightMvcDTO, multipartFile))
        .orElseGet(() -> flightService.createFlight(flightMvcDTO, multipartFile));

    return new RedirectView("/flight/flights");
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
