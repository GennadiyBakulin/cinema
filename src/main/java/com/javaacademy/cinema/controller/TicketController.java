package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRq;
import com.javaacademy.cinema.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

  private final TicketService ticketService;

  @GetMapping("/saled")
  public List<Ticket> getListPurchasedTicket(@RequestParam Integer sessionId) {
    return ticketService.getListPurchasedTicket(sessionId);
  }

  @PostMapping("/booking")
  public ResponseEntity<?> bookingTicket(@RequestBody TicketBookingDtoRq ticketBookingDtoRq) {
    try {
      return ResponseEntity.ok(ticketService.bookingTicket(ticketBookingDtoRq));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }
}
