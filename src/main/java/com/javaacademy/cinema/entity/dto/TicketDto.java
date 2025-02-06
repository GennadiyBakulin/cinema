package com.javaacademy.cinema.entity.dto;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {

  private Place place;
  private Session session;
}
