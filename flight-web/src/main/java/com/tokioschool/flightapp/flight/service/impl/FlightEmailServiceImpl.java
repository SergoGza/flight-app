package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightImageResourceDTO;
import com.tokioschool.flightapp.email.dto.AttachmentDTO;
import com.tokioschool.flightapp.email.dto.EmailDTO;
import com.tokioschool.flightapp.email.service.EmailService;
import com.tokioschool.flightapp.flight.domain.Flight;
import com.tokioschool.flightapp.flight.repository.FlightDAO;
import com.tokioschool.flightapp.flight.service.FlightEmailService;
import com.tokioschool.flightapp.flight.service.FlightImageService;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightEmailServiceImpl implements FlightEmailService {

  private final EmailService emailService;
  private final FlightDAO flightDAO;
  private final FlightImageService flightImageService;
  private final MessageSource messageSource;

  @Override
  public void sendFlightEmail(Long flightId, String to) {

    Flight flight =
        flightDAO
            .findById(flightId)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Flight with id:%s not found".formatted(flightId)));

    Optional<FlightImageResourceDTO> maybeFlightImageResourceDTO =
        Optional.ofNullable(flight.getImage())
            .map(flightImage -> flightImageService.getImage(flightImage.getResourceID()));

    List<AttachmentDTO> attachmentDTOS =
        maybeFlightImageResourceDTO
            .map(
                flightImageResourceDTO ->
                    AttachmentDTO.builder()
                        .contentType(flightImageResourceDTO.getContentType())
                        .filename(flightImageResourceDTO.getFilename())
                        .content(flightImageResourceDTO.getContent())
                        .build())
            .stream()
            .toList();

    Locale localeSpanish = new Locale.Builder().setLanguage("es").build();

    String subjectEn = buildSubject(flight, Locale.ENGLISH);
    String subjectEs = buildSubject(flight, localeSpanish);

    String bodyEn = buildBody(flight, Locale.ENGLISH);
    String bodyEs = buildBody(flight, localeSpanish);

    EmailDTO emailDTO =
        EmailDTO.builder()
            .to(to)
            .subject("%s / %s".formatted(subjectEn, subjectEs))
            .textBody(
                """
                            %s

                            --------------------------------------

                            %s
                            """
                    .formatted(bodyEn, bodyEs))
            .attachments(attachmentDTOS)
            .build();

    emailService.sendEmail(emailDTO);
  }

  protected String buildSubject(Flight flight, Locale locale) {
    String[] subjectArgs = {flight.getNumber()};
    return messageSource.getMessage("flight.email.subject", subjectArgs, locale);
  }

  protected String buildBody(Flight flight, Locale locale) {
    NumberFormat numberFormat = NumberFormat.getInstance(locale);

    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("EEEE MM yyyy HH:mm").withLocale(locale);

    long departureTimeInMinutes =
        ChronoUnit.MINUTES.between(flight.getDepartureTime(), LocalDateTime.now());

    String[] bodyArgs = {
      flight.getNumber(),
      flight.getDeparture().getAcronym(),
      flight.getArrival().getAcronym(),
      dateTimeFormatter.format(flight.getDepartureTime()),
      numberFormat.format(departureTimeInMinutes)
    };

    return messageSource.getMessage("flight.email.body", bodyArgs, locale);
  }
}
