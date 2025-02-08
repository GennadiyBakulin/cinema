package com.javaacademy.cinema.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javaacademy.cinema.entity.Movie;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {

  @JsonIgnoreProperties(ignoreUnknown = true)
  private Integer id;
  private Movie movie;
  private LocalDateTime dateTime;
  private BigDecimal price;
}
