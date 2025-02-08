package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

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
}
