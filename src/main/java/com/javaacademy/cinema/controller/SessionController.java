package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDto;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

  private final SessionRepository sessionRepository;
  private final PlaceRepository placeRepository;
  private final TicketRepository ticketRepository;

  @PostMapping
  public ResponseEntity<?> saveSession(@RequestBody SessionDto sessionDto) {
    try {
      Session session = sessionRepository.saveSession(sessionDto);
      List<Place> placeList = placeRepository.getAllPlace();
      placeList.forEach(place -> ticketRepository.saveTicket(place, session));
      return ResponseEntity.status(HttpStatus.CREATED).body(session);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }
  }
}
