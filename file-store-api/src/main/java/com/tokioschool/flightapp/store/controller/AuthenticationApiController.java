package com.tokioschool.flightapp.store.controller;

import com.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;
import com.tokioschool.flightapp.store.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/api/auth")
public class AuthenticationApiController {

  private final AuthenticationService authenticationService;

  @PostMapping
  public ResponseEntity<AuthenticationResponseDTO> postAuthenticate(
      @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequestDTO));
  }

  @GetMapping("/me")
    public ResponseEntity<AuthenticatedMeResponseDTO> getAuthenticated() {
      return ResponseEntity.ok(authenticationService.getAuthenticated());
  }
}
