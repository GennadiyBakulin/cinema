package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

  private static final String SQL_QUERY_GET_MOVIE_BY_ID = "select * from movie where id = ?";

  private final JdbcTemplate jdbcTemplate;

  public Optional<Movie> findMovieById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_MOVIE_BY_ID,
            this::mapRow,
            id));
  }

  @SneakyThrows
  private Movie mapRow(ResultSet rs, int rowNum) {
    Movie movie = new Movie();
    movie.setId(rs.getInt("id"));
    movie.setName(rs.getString("name"));
    movie.setDescription(rs.getString("description"));
    return movie;
  }
}
