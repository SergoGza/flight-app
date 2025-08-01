package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);
}
