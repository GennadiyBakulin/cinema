package com.javaacademy.cinema.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Session {

  private Integer id;
  private Movie movie;
  private LocalDateTime dateTime;
  private BigDecimal price;
}
