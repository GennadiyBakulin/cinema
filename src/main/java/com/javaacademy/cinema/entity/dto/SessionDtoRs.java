package com.javaacademy.cinema.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDtoRs {

  private Integer id;
  @JsonProperty("movie_name")
  private String movieName;
  private LocalDateTime date;
  private BigDecimal price;
}
