package com.tokioschool.flightapp.flight.repository;

import com.tokioschool.flightapp.flight.domain.Role;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleJdbcDAO {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Role> rowMapper =
      (rs, rowNumber) -> Role.builder().id(rs.getLong("id")).name(rs.getString("name")).build();

  public int countRoles() {
    String sql = "SELECT count(1) as count FROM roles";
    RowMapper<Integer> counterRowMapper = ((rs, rowNumber) -> rs.getInt("count"));

    return namedParameterJdbcTemplate.queryForObject(sql, Map.of(), counterRowMapper);
  }

  public Long insertRole(Role role) {

    SimpleJdbcInsert simpleJdbcInsert =
        new SimpleJdbcInsert(jdbcTemplate).withTableName("roles").usingGeneratedKeyColumns("id");

    Map<String, String> parameters = Map.of("name", role.getName());

    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
  }

  public Optional<Role> findRoleById(Long id) {

    String sql =
"""

        SELECT
        id,
        name
        FROM roles
        WHERE id = :id
""";

    Role role = namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), rowMapper);
    return Optional.ofNullable(role);
  }

  public int deleteRoleByName(String name) {
    String sql = "DELETE FROM roles WHERE name = :name";

    return namedParameterJdbcTemplate.update(sql, Map.of("name" , name));
  }
}
