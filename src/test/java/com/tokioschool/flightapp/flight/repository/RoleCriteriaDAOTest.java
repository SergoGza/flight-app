package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto:create-drop"
        })
class RoleCriteriaDAOTest {

  @Autowired private RoleDAO roleDAO;

  @Autowired private EntityManager entityManager;

  private RoleCriteriaDAO roleCriteriaDAO;

  @BeforeEach
  void beforeEach() {
    roleCriteriaDAO = new RoleCriteriaDAO(entityManager);

    Role role1 = Role.builder().name("role1").build();

    roleDAO.save(role1);
  }

  @Test
  void givenRoles_whenFindByIdAndName_thenOk(){
    Optional<Role> maybeRole = roleCriteriaDAO.findRoleByName("role1");
    Assertions.assertTrue(maybeRole.isPresent());
  }

  @Test
  void givenRoles_whenFindByNonExistingIdAndName_thenOk(){

    Optional<Role> maybeRole = roleCriteriaDAO.findRoleByName("role100");
    Assertions.assertTrue(maybeRole.isEmpty());
  }
}
