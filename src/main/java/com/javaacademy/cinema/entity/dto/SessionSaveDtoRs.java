package com.javaacademy.cinema.entity.dto;

import com.javaacademy.cinema.entity.Movie;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionSaveDtoRs {

  private Integer id;
  private Movie movie;
  private LocalDateTime dateTime;
  private BigDecimal price;
}
