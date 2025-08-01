package com.tokioschool.flightapp.flight.domain;

import com.tokioschool.flightapp.core.repository.TsidGenerator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id @TsidGenerator private String id;

  @CreationTimestamp private LocalDateTime created;

  private String name;
  private String surname;
  private String email;
  private String password;
  private LocalDateTime lastLogin;

  @ManyToMany (fetch = FetchType.EAGER)
  @JoinTable (
          name = "users_with_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn (name = "role_id"))
  private Set <Role> roles;

}
