package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDto;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

  public Session sessionDtoToEntity(SessionDto sessionDto, Integer id) {
    return new Session(id, sessionDto.getMovie(), sessionDto.getDateTime(), sessionDto.getPrice());
  }

  public SessionDto entityToSessionDto(Session session) {
    return new SessionDto(session.getId(), session.getMovie(), session.getDateTime(),
        session.getPrice());
  }
}
