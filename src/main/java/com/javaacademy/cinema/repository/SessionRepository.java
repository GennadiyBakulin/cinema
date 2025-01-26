package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

  private static final String SQL_QUERY_GET_SESSION_BY_ID = "select * from session where id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final MovieRepository movieRepository;

  public Optional<Session> findSessionById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_SESSION_BY_ID,
            this::mapRow,
            id));
  }

  @SneakyThrows
  private Session mapRow(ResultSet rs, int rowNum) {
    Session session = new Session();
    session.setId(rs.getInt("id"));
    session.setMovie(movieRepository.findMovieById(rs.getInt("movie_id")).orElse(new Movie()));
    session.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
    session.setPrice(rs.getBigDecimal("price"));
    return session;
  }
}
