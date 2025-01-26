package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

  private static final String SQL_QUERY_GET_TICKET_BY_ID = "select * from ticket where id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final PlaceRepository placeRepository;
  private final SessionRepository sessionRepository;

  public Optional<Ticket> findTicketById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_TICKET_BY_ID,
            this::mapRow,
            id));
  }

  @SneakyThrows
  private Ticket mapRow(ResultSet rs, int rowNum) {
    Ticket ticket = new Ticket();
    ticket.setId(rs.getInt("id"));
    ticket.setPlace(placeRepository.findPlaceById(rs.getInt("place_id")).orElse(new Place()));
    ticket.setSession(
        sessionRepository.findSessionById(rs.getInt("session_id")).orElse(new Session()));
    ticket.setPurchased(rs.getBoolean("purchased"));
    return ticket;
  }
}
