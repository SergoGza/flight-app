package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.FlightImageResourceDTO;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FlightImageService {

    FlightImage saveImage(MultipartFile multipartFile);
    FlightImageResourceDTO getImage(UUID resourceId);
    void deleteImage(UUID resourceId);

}
