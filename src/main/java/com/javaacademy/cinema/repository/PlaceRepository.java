package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Place;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

  private static final String SQL_QUERY_GET_PLACE_BY_ID = "select * from place where id = ?";

  private final JdbcTemplate jdbcTemplate;

  public Optional<Place> findPlaceById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_PLACE_BY_ID,
            this::mapRow,
            id));
  }

  @SneakyThrows
  private Place mapRow(ResultSet rs, int rowNum) {
    Place place = new Place();
    place.setId(rs.getInt("id"));
    place.setName(rs.getString("name"));
    return place;
  }
}
