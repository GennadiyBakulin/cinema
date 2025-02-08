package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDto;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;
  private final PlaceRepository placeRepository;
  private final TicketRepository ticketRepository;
  private final SessionMapper mapper;

  public Session saveSession(SessionDto sessionDto) {
    Session session = sessionRepository.saveSession(sessionDto);
    List<Place> placeList = placeRepository.getAllPlace();
    placeList.forEach(place -> ticketRepository.saveTicket(place, session));
    return session;
  }

  public List<SessionDto> getAllSession() {
    return sessionRepository.getAllSession().stream().map(mapper::entityToSessionDto).toList();
  }
}
