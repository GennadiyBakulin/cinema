package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.mapper.TicketMapper;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

  private static final String SQL_QUERY_GET_TICKET_BY_ID =
      "select * from ticket where id = ?";
  private static final String SQL_QUERY_CREATE_TICKET_AND_RETURN_ID =
      "insert into ticket (place_id, session_id, purchased) values(?, ?, ?) returning id";
  private static final String SQL_QUERY_CHANGE_STATUS_TICKET_ON_PURCHASED =
      "update ticket set purchased = true where id = ?";
  private static final String SQL_QUERY_GET_LIST_PURCHASED_TICKET_ON_SESSION =
      "select * from ticket where purchased and session_id = ?";
  private static final String SQL_QUERY_GET_LIST_NOT_PURCHASED_TICKET_ON_SESSION =
      "select * from ticket where !purchased and session_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final PlaceRepository placeRepository;
  private final SessionRepository sessionRepository;
  private final TicketMapper mapper;

  public Ticket saveTicket(Place place, Session session) {
    Integer id = jdbcTemplate.queryForObject(
        SQL_QUERY_CREATE_TICKET_AND_RETURN_ID,
        Integer.class,
        place.getId(), session.getId(), false);
    return new Ticket(id, place, session, false);
  }


  public Optional<Ticket> findTicketById(Integer ticketId) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_TICKET_BY_ID,
            this::mapToTicket,
            ticketId));
  }

  public void changeTicketStatusByIdToPurchased(Integer ticketId) {
    Ticket ticket = findTicketById(ticketId).orElseThrow(
        () -> new RuntimeException("Билет по данному id не найден!"));
    checkTicketPurchasedStatus(ticket);
    jdbcTemplate.update(
        SQL_QUERY_CHANGE_STATUS_TICKET_ON_PURCHASED,
        ticketId);
  }

  public List<Ticket> getListPurchasedTicketOnSession(Integer sessionId) {
    return jdbcTemplate.query(
        SQL_QUERY_GET_LIST_PURCHASED_TICKET_ON_SESSION,
        this::mapToTicket,
        sessionId);
  }

  public List<Ticket> getListNotPurchasedTicketOnSession(Integer sessionId) {
    return jdbcTemplate.query(
        SQL_QUERY_GET_LIST_NOT_PURCHASED_TICKET_ON_SESSION,
        this::mapToTicket,
        sessionId);
  }

  @SneakyThrows
  private Ticket mapToTicket(ResultSet rs, int rowNum) {
    Ticket ticket = new Ticket();
    ticket.setId(rs.getInt("id"));
    ticket.setPlace(placeRepository.findPlaceById(rs.getInt("place_id")).orElse(new Place()));
    ticket.setSession(
        sessionRepository.findSessionById(rs.getInt("session_id")).orElse(new Session()));
    ticket.setPurchased(rs.getBoolean("purchased"));
    return ticket;
  }

  private void checkTicketPurchasedStatus(Ticket ticket) {
    if (ticket.getPurchased()) {
      throw new RuntimeException("Билет уже куплен!");
    }
  }
}
