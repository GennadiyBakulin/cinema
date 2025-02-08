package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.mapper.SessionMapper;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

  private static final String SQL_QUERY_GET_SESSION_BY_ID = "select * from session where id = ?";
  private static final String SQL_QUERY_CREATE_SESSION_AND_RETURN_ID =
      "insert into session (movie_id, datetime, price) values(?, ?, ?) returning id";
  private static final String SQL_QUERY_GET_ALL_SESSION = "select * from session";

  private final JdbcTemplate jdbcTemplate;
  private final MovieRepository movieRepository;
  private final SessionMapper mapper;

  public Session saveSession(SessionDtoRq sessionDtoRq) {
    Integer id = jdbcTemplate.queryForObject(
        SQL_QUERY_CREATE_SESSION_AND_RETURN_ID,
        Integer.class,
        sessionDtoRq.getMovie().getId(), sessionDtoRq.getDateTime(), sessionDtoRq.getPrice());
    return mapper.sessionDtoToEntity(sessionDtoRq, id);
  }

  public Optional<Session> findSessionById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_SESSION_BY_ID,
            this::mapToSession,
            id));
  }

  public List<Session> getAllSession() {
    return jdbcTemplate.query(SQL_QUERY_GET_ALL_SESSION, this::mapToSession);
  }

  @SneakyThrows
  private Session mapToSession(ResultSet rs, int rowNum) {
    Session session = new Session();
    session.setId(rs.getInt("id"));
    session.setMovie(movieRepository.findMovieById(rs.getInt("movie_id")).orElseThrow());
    session.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
    session.setPrice(rs.getBigDecimal("price"));
    return session;
  }
}
