package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDto;
import com.javaacademy.cinema.service.SessionService;
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

  private final SessionService sessionService;

  @PostMapping
  public ResponseEntity<?> saveSession(@RequestBody SessionDto sessionDto) {
    try {
      Session session = sessionService.saveSession(sessionDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(session);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }
  }
}
