package com.tokioschool.flightapp.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokioschool.flightapp.store.dto.ResourceContentDTO;
import com.tokioschool.flightapp.store.security.StoreApiSecurityConfiguration;
import com.tokioschool.flightapp.store.service.StoreService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(controllers = ResourceApiController.class)
//@AutoConfigureMockMvc
@ContextConfiguration(
    classes = {
      ResourceApiController.class,
      StoreApiSecurityConfiguration.class,
      ResourceApiControllerTest.Init.class
    })
class ResourceApiControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private StoreService storeService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "username", authorities = "read-resource")
  void givenExistingResource_whenGet_thenRetrievedOk() throws Exception {

    ResourceContentDTO resourceContentDTO =
        ResourceContentDTO.builder()
            .resourceId(UUID.randomUUID())
            .description("description")
            .contentType("text/plain")
            .filename("hello.txt")
            .size("Hello".length())
            .content("Hello".getBytes(StandardCharsets.UTF_8))
            .build();

    Mockito.when(storeService.findResource(resourceContentDTO.getResourceId()))
        .thenReturn(Optional.of(resourceContentDTO));

    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(
                    "/store/api/resources/{resourceId}", resourceContentDTO.getResourceId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    ResourceContentDTO response =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), ResourceContentDTO.class);

    Map<String, Object> values =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    String base64 = Base64.getEncoder().encodeToString(resourceContentDTO.getContent());

    Assertions.assertThat(values).containsEntry("content", base64);
    Assertions.assertThat(response.getContent()).isEqualTo(resourceContentDTO.getContent());
  }

  @TestConfiguration
  public static class Init {
    @Bean
    public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withSecretKey(new SecretKeySpec("SECRET".getBytes(), "HMAC")).build();
    }
  }
}
