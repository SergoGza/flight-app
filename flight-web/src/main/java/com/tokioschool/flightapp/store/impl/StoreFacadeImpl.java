package com.tokioschool.flightapp.store.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokioschool.flightapp.store.StoreFacade;
import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.dto.ResourceIdDTO;
import jakarta.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreFacadeImpl implements StoreFacade {

  // private final RestTemplate restTemplate;
  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  @Override
  public Optional<ResourceIdDTO> saveResource(
      MultipartFile multipartFile, @Nullable String description) {

    HashMap<String, String> descriptionMap = new HashMap<>();
    descriptionMap.put("description", description);

    String descriptionBody;

    try {
      descriptionBody = objectMapper.writeValueAsString(descriptionMap);
    } catch (JsonProcessingException jpe) {
      descriptionBody = "";
    }

    MediaType mediaType;

    try {
      mediaType = MediaType.parseMediaType(multipartFile.getContentType());
    } catch (InvalidMediaTypeException imte) {
      mediaType = MediaType.APPLICATION_OCTET_STREAM;
    }

    try {
      MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

      HttpHeaders descriptionHeaders = new HttpHeaders();
      descriptionHeaders.setContentType(MediaType.APPLICATION_JSON);
      parts.add("description", new HttpEntity<>(descriptionBody, descriptionHeaders));

      HttpHeaders contentHeaders = new HttpHeaders();
      contentHeaders.setContentType(mediaType);
      parts.add("content", new HttpEntity<>(multipartFile.getResource(), contentHeaders));

      ResourceIdDTO resourceIdDTO =
          restClient
              .post()
              .uri("/store/api/resources")
              .contentType(MediaType.MULTIPART_FORM_DATA)
              .body(parts)
              .retrieve()
              .body(ResourceIdDTO.class);

      return Optional.of(resourceIdDTO);
    } catch (Exception e) {
      log.error("Exception in saveResource", e);
    }

    return Optional.empty();
  }

  @Override
  public Optional<ResourceContentDTO> findResource(UUID resourceid) {

    try {

      ResourceContentDTO resourceContentDTO =
          restClient
              .get()
              .uri("/store/api/resources/{resourceId}", resourceid)
              .retrieve()
              .body(ResourceContentDTO.class);

      return Optional.of(resourceContentDTO);

      //      ResourceContentDTO resourceContentDTO = restTemplate.getForObject(
      //              "http://localhost:8081/store/api/resources/{resourceId}",
      //              ResourceContentDTO.class,
      //              resourceid);
      //
      //      return Optional.of(resourceContentDTO);

    } catch (Exception e) {

      log.error("Exception in findResource", e);
    }
    return Optional.empty();
  }

  @Override
  public void deleteResource(UUID resourceID) {

    restClient
        .delete()
        .uri("/store/api/resources/{resourceId}", resourceID)
        .retrieve()
        .toBodilessEntity();
  }
}
