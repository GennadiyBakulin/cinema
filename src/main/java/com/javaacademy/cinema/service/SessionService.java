package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
import com.javaacademy.cinema.exception.NotFoundPlaceById;
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

  public List<SessionDtoRs> getAllSession() {
    return sessionRepository.getAllSession().stream().map(mapper::entityToSessionDtoRs).toList();
  }

  public List<String> getFreePlaceOnSession(Integer sessionId) {
    List<Ticket> listNotPurchasedTicketOnSession = ticketRepository.
        getListNotPurchasedTicketOnSession(sessionId);
    List<Place> listFreePlace = listNotPurchasedTicketOnSession.stream()
        .map(ticket -> placeRepository.findPlaceById(ticket.getPlace().getId())
            .orElseThrow(() -> new NotFoundPlaceById("Не найдено место по указанному Id!")))
        .toList();
    return listFreePlace.stream().map(Place::getName).toList();
  }
}
