package com.tokioschool.flightapp.store.controller;

import com.tokioschool.flightapp.core.exception.InternalExceptionError;
import com.tokioschool.flightapp.core.exception.NotFoundException;
import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.dto.ResourceCreateRequestDTO;
import com.tokioschool.flightapp.store.dto.ResourceIdDTO;
import com.tokioschool.flightapp.store.service.StoreService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/api/resources")
@Validated
public class ResourceApiController {
  private final StoreService storeService;

  @GetMapping("/{resourceId}")
  public ResponseEntity<ResourceContentDTO> getResourceContent(
      @PathVariable("resourceId") UUID resourceId) {
    ResourceContentDTO resourceContentDTO =
        storeService
            .findResource(resourceId)
            .orElseThrow(
                () -> new NotFoundException("Resource with id:%s not found".formatted(resourceId)));

    return ResponseEntity.ok(resourceContentDTO);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResourceIdDTO> createResource(
      @RequestPart("description") ResourceCreateRequestDTO resourceCreateDescriptionDTO,
      @RequestPart("content") MultipartFile multipartFile) {

    ResourceIdDTO resourceIdDTO =
        storeService
            .saveResource(multipartFile, resourceCreateDescriptionDTO.getDescription())
            .orElseThrow(
                () -> new InternalExceptionError("There's been an error, try it again later"));

    return ResponseEntity.status(HttpStatus.CREATED).body(resourceIdDTO);
  }

  @DeleteMapping("/{resourceId}")
  public ResponseEntity<Void> deleteResource(@PathVariable("resourceId") UUID resourceId) {

    storeService.deleteResource(resourceId);
    return ResponseEntity.noContent().build();
  }
}
