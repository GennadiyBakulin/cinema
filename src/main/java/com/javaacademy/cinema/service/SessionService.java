package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.SessionDto;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
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

  public List<String> getFreePlaceOnSession(Integer id) {
    Optional<Session> session = sessionRepository.findSessionById(id);
    if (session.isEmpty()) {
      throw new RuntimeException("Сеанс с указанным номером не найден!");
    }
    List<Ticket> listNotPurchasedTicketOnSession = ticketRepository.
        getListNotPurchasedTicketOnSession(session.get().getId());
    List<Place> listFreePlace = listNotPurchasedTicketOnSession.stream()
        .map(ticket -> placeRepository.findPlaceById(ticket.getPlace().getId()).orElse(new Place()))
        .toList();

    return listFreePlace.stream().map(Place::getName).toList();
  }
}
