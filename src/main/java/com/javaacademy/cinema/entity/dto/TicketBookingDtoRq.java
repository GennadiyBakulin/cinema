package com.javaacademy.cinema.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookingDtoRq {

  @JsonProperty("session_id")
  private Integer sessionId;
  @JsonProperty("place_name")
  private String placeName;
}
