package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Role;
import com.tokioschool.flightapp.flight.domain.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@DataJpaTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto:create-drop"
    })
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserDAOTest {

  @Autowired private RoleDAO roleDAO;
  @Autowired private UserDAO userDAO;
  @Autowired private TransactionTemplate transactionTemplate;

  @BeforeEach
  void beforeEach() {

    Role role1 = Role.builder().name("role1").build();
    Role role2 = Role.builder().name("role2").build();

    roleDAO.saveAll(List.of(role1, role2));

    User user1 =
        User.builder()
            .email("user1@bla.com")
            .roles(new HashSet<>(Set.of(role1, role2)))
            .password("password")
            .name("name1")
            .surname("surname1")
            .build();

    User user2 =
        User.builder()
            .email("user2@bla.com")
            .roles(new HashSet<>())
            .password("password")
            .name("name2")
            .surname("surname2")
            .build();

    userDAO.saveAll(List.of(user1, user2));
  }

  @AfterEach
  void afterEach() {
    userDAO.deleteAll();
    roleDAO.deleteAll();
  }

  @Test
  void givenTwoUsers_whenFindAll_thenReturnAll() {

    transactionTemplate.executeWithoutResult(transactionStatus -> {

      List<User> users = userDAO.findAll();

      Assertions.assertThat(users).hasSize(2);

      Assertions.assertThat(
                      users.stream()
                              .filter(user -> user.getEmail().equals("user1@bla.com"))
                              .findFirst()
                              .get())
              .returns("name1", User::getName)
              .satisfies(user -> Assertions.assertThat(user.getCreated()).isNotNull())
              .satisfies(user -> Assertions.assertThat(user.getId()).isNotNull())
              .satisfies(
                      user ->
                              Assertions.assertThat(user.getRoles().stream().map(Role::getName).toList())
                                      .containsExactlyInAnyOrder("role1", "role2"));

    });


  }

  @Test
  void givenUser_whenDeleted_thenManyToManyIsDeleted() {

    User user = userDAO.findByEmail("user1@bla.com").get();

    userDAO.delete(user);

    List<User> users = userDAO.findAll();

    Assertions.assertThat(users).hasSize(1);
  }
}
