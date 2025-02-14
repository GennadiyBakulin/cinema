package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.TicketPurchasedDtoRs;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

  public TicketPurchasedDtoRs entityToTicketPurchasedDtoRs(Ticket ticket) {
    return new TicketPurchasedDtoRs(ticket.getId(), ticket.getPlace(), ticket.getSession());
  }
}
