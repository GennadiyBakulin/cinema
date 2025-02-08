package com.javaacademy.cinema.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookingDtoRs {

  @JsonProperty("ticket_id")
  private Integer ticketId;
  @JsonProperty("place_name")
  private String placeName;
  @JsonProperty("movie_name")
  private String movieName;
  private LocalDateTime date;
}
