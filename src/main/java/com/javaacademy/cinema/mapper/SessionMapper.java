package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

  public SessionDtoRs entityToSessionDtoRs(Session session) {
    return new SessionDtoRs(session.getId(), session.getMovie().getName(), session.getDateTime(),
        session.getPrice());
  }
}
