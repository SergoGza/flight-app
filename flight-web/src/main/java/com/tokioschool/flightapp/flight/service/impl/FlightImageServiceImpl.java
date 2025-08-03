package com.tokioschool.flightapp.flight.service.impl;

import com.tokioschool.flightapp.dto.FlightImageResourceDTO;
import com.tokioschool.flightapp.flight.domain.FlightImage;
import com.tokioschool.flightapp.flight.service.FlightImageService;
import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.dto.ResourceIdDTO;
import com.tokioschool.flightapp.store.service.StoreService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FlightImageServiceImpl implements FlightImageService {

  private final StoreService storeService;

  @Override
  public FlightImage saveImage(MultipartFile multipartFile) {

    ResourceIdDTO resourceIdDTO =
        storeService
            .saveResource(multipartFile, "flight-app")
            .orElseThrow(() -> new IllegalStateException("Resource not saved in store"));

    return FlightImage.builder()
        .contentType(multipartFile.getContentType())
        .size((int) multipartFile.getSize())
        .resourceID(resourceIdDTO.getResourceId())
        .filename(multipartFile.getOriginalFilename())
        .build();
  }

  @Override
  public FlightImageResourceDTO getImage(UUID resourceId) {

    ResourceContentDTO resourceContentDTO =
        storeService
            .findResource(resourceId)
            .orElseThrow(() -> new IllegalStateException("Resource not found in store"));

    return FlightImageResourceDTO.builder()
        .content(resourceContentDTO.getContent())
        .contentType(resourceContentDTO.getContentType())
        .filename(resourceContentDTO.getFilename())
        .size(resourceContentDTO.getSize())
        .build();
  }

  @Override
  public void deleteImage(UUID resourceId) {

    storeService.deleteResource(resourceId);
  }
}
