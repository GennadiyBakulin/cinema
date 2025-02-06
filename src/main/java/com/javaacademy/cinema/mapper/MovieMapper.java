package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

  public Movie movieDtoToEntity(MovieDto movieDto, Integer id) {
    return new Movie(id, movieDto.getName(), movieDto.getDescription());
  }
}
