package com.tokioschool.flightapp.store.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokioschool.flightapp.store.config.StoreConfigurationProperties;
import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.dto.ResourceIdDTO;
import com.tokioschool.flightapp.store.service.StoreService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class StoreServiceImplTest {

  private StoreService storeService;
  private final ObjectMapper objectMapper = new ObjectMapper();
  @TempDir private Path tempBasePath;

  @BeforeEach
  void beforeEach() {

    StoreConfigurationProperties storeConfigurationProperties =
        new StoreConfigurationProperties(tempBasePath);

    storeService = new StoreServiceImpl(storeConfigurationProperties, objectMapper);
  }

  @Test
  void givenFile_whenStored_thenOk() throws IOException {

    MockMultipartFile file =
        new MockMultipartFile(
            "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World!".getBytes());

    Optional<ResourceIdDTO> resourceIdDTO = storeService.saveResource(file, "description");

    Path resourceContent = tempBasePath.resolve(resourceIdDTO.get().getResourceId().toString());
    ResourceContentDTO resourceDescription =
        objectMapper.readValue(
            tempBasePath.resolve(resourceIdDTO.get().getResourceId() + ".json").toFile(), ResourceContentDTO.class);

    assertThat(resourceContent)
        .satisfies(path -> assertThat(Files.exists(path)).isTrue())
        .satisfies(
            path -> assertThat(Files.readAllBytes(path)).isEqualTo("Hello World!".getBytes()));

    assertThat(resourceDescription)
        .returns("hello.txt", ResourceContentDTO::getFilename)
        .returns("text/plain", ResourceContentDTO::getContentType)
        .returns("description", ResourceContentDTO::getDescription)
        .returns("Hello World!".length(), ResourceContentDTO::getSize);
  }
}
