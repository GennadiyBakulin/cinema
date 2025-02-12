package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Place;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

  private static final String SQL_QUERY_GET_PLACE_BY_ID = "select * from place where id = ?";
  private static final String SQL_QUERY_GET_ALL_PLACE = "select * from place";

  private final JdbcTemplate jdbcTemplate;

  public Optional<Place> findPlaceById(Integer id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              SQL_QUERY_GET_PLACE_BY_ID,
              this::mapToPlace,
              id));
    } catch (
        EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public List<Place> getAllPlace() {
    return jdbcTemplate.query(SQL_QUERY_GET_ALL_PLACE, this::mapToPlace);
  }

  @SneakyThrows
  private Place mapToPlace(ResultSet rs, int rowNum) {
    Place place = new Place();
    place.setId(rs.getInt("id"));
    place.setName(rs.getString("name"));
    return place;
  }
}
