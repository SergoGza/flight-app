package com.tokioschool.flightapp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String id;
  private String name;
  private String surname;
  private String email;
  private LocalDateTime lastLogin;
  private List<String> roles;
}
