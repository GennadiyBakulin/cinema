package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRq;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRs;
import com.javaacademy.cinema.exception.NotChangeStatusTicket;
import com.javaacademy.cinema.exception.NotFoundSessionById;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final SessionRepository sessionRepository;

  public Ticket saveTicket(Place place, Session session) {
    return ticketRepository.saveTicket(place, session);
  }

  public Ticket findTicketById(Integer ticketId) {
    return ticketRepository.findTicketById(ticketId).orElseThrow();
  }

  public void changeTicketStatusByIdToPurchased(Integer ticketId) {
    ticketRepository.changeTicketStatusByIdToPurchased(ticketId);
  }

  public List<Ticket> getListPurchasedTicket(Integer sessionId) {
    return ticketRepository.getListPurchasedTicketOnSession(sessionId);
  }

  public List<Ticket> getListNotPurchasedTicket(Integer sessionId) {
    return ticketRepository.getListNotPurchasedTicketOnSession(sessionId);
  }

  public TicketBookingDtoRs bookingTicket(TicketBookingDtoRq ticketBookingDtoRq) {
    Session session = sessionRepository.findSessionById(ticketBookingDtoRq.getSessionId())
        .orElseThrow(() -> new NotFoundSessionById("Сеанс с указанным Id не найден!"));
    List<Ticket> listNotPurchasedTicket = getListNotPurchasedTicket(session.getId());
    Ticket ticketNotPurchased = listNotPurchasedTicket.stream()
        .filter(ticket -> ticket.getPlace().getName().equals(ticketBookingDtoRq.getPlaceName()))
        .findFirst()
        .orElseThrow(() -> new NotChangeStatusTicket("Билет не найден или уже был выкуплен!"));
    changeTicketStatusByIdToPurchased(ticketNotPurchased.getId());
    return new TicketBookingDtoRs(
        ticketNotPurchased.getId(),
        ticketBookingDtoRq.getPlaceName(),
        session.getMovie().getName(),
        session.getDateTime()
    );
  }
}
