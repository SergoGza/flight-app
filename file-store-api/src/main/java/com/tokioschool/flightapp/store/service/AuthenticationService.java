package com.tokioschool.flightapp.store.service;

import com.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import com.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO authenticate (AuthenticationRequestDTO authenticationRequestDTO);
    AuthenticatedMeResponseDTO getAuthenticated();

}
