package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleCriteriaDAO {

  private final EntityManager entityManager;

  public Optional<Role> findRoleByName(String name) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Role> query = criteriaBuilder.createQuery(Role.class);
    Root<Role> root = query.from(Role.class);

    query.where(criteriaBuilder.equal(root.get("name"), name));

    try {
      Role singleResult = entityManager.createQuery(query).getSingleResult();
      return Optional.of(singleResult);
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }
}
