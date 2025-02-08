package com.javaacademy.cinema.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookingDtoRq {

  private Integer sessionId;
  private String placeName;
}
