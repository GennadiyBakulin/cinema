package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.repository.MovieRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository repository;
  private final MovieMapper mapper;

  public Movie saveMovie(MovieDto movieDto) {
    return repository.saveMovie(movieDto);
  }

  public Movie findMovieById(Integer id) {
    return repository.findMovieById(id).orElseThrow();
  }

  public List<MovieDto> getAllMovies() {
    List<Movie> movieList = repository.getAllMovies();
    return movieList.stream().map(mapper::entityToMovieDto).toList();
  }
}
