package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.SessionDto;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.SessionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;
  private final TicketService ticketService;
  private final PlaceService placeService;
  private final SessionMapper mapper;

  public Session saveSession(SessionDto sessionDto) {
    Session session = sessionRepository.saveSession(sessionDto);
    List<Place> placeList = placeService.getAllPlace();
    placeList.forEach(place -> ticketService.saveTicket(place, session));
    return session;
  }

  public Session findSessionById(Integer id) {
    return sessionRepository.findSessionById(id).orElseThrow();
  }

  public List<SessionDto> getAllSession() {
    return sessionRepository.getAllSession().stream().map(mapper::entityToSessionDto).toList();
  }

  public List<String> getFreePlaceOnSession(Integer id) {
    List<Ticket> listNotPurchasedTicketOnSession = ticketService.
        getListNotPurchasedTicket(id);
    List<Place> listFreePlace = listNotPurchasedTicketOnSession.stream()
        .map(ticket -> placeService.findPlaceById(ticket.getPlace().getId()))
        .toList();
    return listFreePlace.stream().map(Place::getName).toList();
  }
}
