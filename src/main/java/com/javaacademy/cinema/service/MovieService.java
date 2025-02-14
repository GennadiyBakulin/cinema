package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.entity.dto.MovieSaveDtoRs;
import com.javaacademy.cinema.exception.AccessDenied;
import com.javaacademy.cinema.exception.NotFoundMovieById;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.repository.MovieRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final MovieMapper mapper;

  @Value("${header.user-token.value}")
  private String tokenValue;

  public MovieSaveDtoRs saveMovie(MovieDto movieDto) {
    Movie movie = movieRepository.saveMovie(movieDto);
    return mapper.entityToMovieSaveDtoRs(movie);
  }

  public Movie findMovieById(Integer id) {
    return movieRepository.findMovieById(id)
        .orElseThrow(() -> new NotFoundMovieById("Не найден фильм с указанным Id!"));
  }

  public List<MovieDto> getAllMovies() {
    List<Movie> movieList = movieRepository.getAllMovies();
    return movieList.stream().map(mapper::entityToMovieDto).toList();
  }

  public void tokenValidate(String token) {
    if (Objects.isNull(token) || !token.equals(tokenValue)) {
      throw new AccessDenied("Отсутствует либо не верно указан токен!");
    }
  }
}
