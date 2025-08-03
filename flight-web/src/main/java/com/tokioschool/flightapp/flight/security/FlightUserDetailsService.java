package com.tokioschool.flightapp.flight.security;

import com.tokioschool.flightapp.dto.UserDTO;
import com.tokioschool.flightapp.flight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Pair<UserDTO, String> userAndPassword =
        userService
            .findUserAndPasswordByEmail(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User with username:%s not found".formatted(username)));

    return toUserDetails(userAndPassword.getLeft(), userAndPassword.getRight());
  }

  private UserDetails toUserDetails(UserDTO userDTO, String password) {
    return new User(
        userDTO.getEmail(),
        password,
        userDTO.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
  }
}
