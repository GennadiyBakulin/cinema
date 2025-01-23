package com.javaacademy.cinema.entity;

import lombok.Data;

@Data
public class Ticket {

  private Integer id;
  private Place place;
  private Session session;
  private Boolean purchased;
}
