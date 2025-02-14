package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.entity.dto.MovieSaveDtoRs;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

  public MovieDto entityToMovieDto(Movie movie) {
    return new MovieDto(movie.getName(), movie.getDescription());
  }

  public MovieSaveDtoRs entityToMovieSaveDtoRs(Movie movie) {
    return new MovieSaveDtoRs(movie.getId(), movie.getName(), movie.getDescription());
  }
}
