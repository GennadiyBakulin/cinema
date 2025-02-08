package com.javaacademy.cinema.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDtoRq {

  private Integer movieId;
  private LocalDateTime dateTime;
  private BigDecimal price;
}
