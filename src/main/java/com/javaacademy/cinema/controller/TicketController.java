package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

  private final TicketRepository ticketRepository;

  @GetMapping("/saled")
  public List<Ticket> getPurchasedTicket(@RequestParam Integer sessionId) {
    return ticketRepository.getListPurchasedTicketOnSession(sessionId);
  }
}
