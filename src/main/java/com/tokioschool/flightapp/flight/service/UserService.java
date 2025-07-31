package com.tokioschool.flightapp.flight.service;

import com.tokioschool.flightapp.dto.UserDTO;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

public interface UserService {

  Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String email);

  Optional<UserDTO> findByEmail(String email);
}
