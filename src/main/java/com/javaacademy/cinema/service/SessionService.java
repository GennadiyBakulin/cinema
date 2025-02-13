package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.SessionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepository sessionRepository;
  private final SessionService sessionService;
  private final TicketService ticketService;
  private final PlaceService placeService;
  private final MovieService movieService;
  private final SessionMapper mapper;

  public Session saveSession(SessionDtoRq sessionDtoRq) {
    Movie movie = movieService.findMovieById(sessionDtoRq.getMovieId());
    Session session = sessionRepository.saveSession(sessionDtoRq, movie);
    List<Place> placeList = placeService.getAllPlace();
    placeList.forEach(place -> ticketService.saveTicket(place, session));
    return session;
  }

  public Session findSessionById(Integer id) {
    return sessionService.findSessionById(id);
  }

  public List<SessionDtoRs> getAllSession() {
    return sessionRepository.getAllSession().stream().map(mapper::entityToSessionDtoRs).toList();
  }

  public List<String> getFreePlaceOnSession(Integer sessionId) {
    List<Ticket> listNotPurchasedTicketOnSession = ticketService.
        getListNotPurchasedTicket(sessionId);
    List<Place> listFreePlace = listNotPurchasedTicketOnSession.stream()
        .map(ticket -> placeService.findPlaceById(ticket.getPlace().getId()))
        .toList();
    return listFreePlace.stream().map(Place::getName).toList();
  }
}
