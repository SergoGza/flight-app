package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {}
