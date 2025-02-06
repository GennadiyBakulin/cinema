package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

  public List<Ticket> getListPurchasedTicket(Integer sessionId) {
    return ticketRepository.getListPurchasedTicketOnSession(sessionId);
  }
}
