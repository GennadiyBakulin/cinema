package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

  public Ticket ticketDtoToEntity(TicketDto ticketDto, Integer id) {
    return new Ticket(id, ticketDto.getPlace(), ticketDto.getSession(), ticketDto.getPurchased());
  }
}
