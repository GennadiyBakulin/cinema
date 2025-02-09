package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
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
  private final TicketRepository ticketRepository;
  private final PlaceRepository placeRepository;
  private final SessionMapper mapper;

  public Session saveSession(SessionDtoRq sessionDtoRq) {
    Session session = sessionRepository.saveSession(sessionDtoRq);
    List<Place> placeList = placeRepository.getAllPlace();
    placeList.forEach(place -> ticketRepository.saveTicket(place, session));
    return session;
  }

  public Session findSessionById(Integer id) {
    return sessionRepository.findSessionById(id).orElseThrow();
  }

  public List<SessionDtoRs> getAllSession() {
    return sessionRepository.getAllSession().stream().map(mapper::entityToSessionDtoRs).toList();
  }

  public List<String> getFreePlaceOnSession(Integer id) {
    List<Ticket> listNotPurchasedTicketOnSession = ticketRepository.
        getListNotPurchasedTicketOnSession(id);
    List<Place> listFreePlace = listNotPurchasedTicketOnSession.stream()
        .map(ticket -> placeRepository.findPlaceById(ticket.getPlace().getId()).orElseThrow())
        .toList();
    return listFreePlace.stream().map(Place::getName).toList();
  }
}
