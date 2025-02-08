package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.exception.NotUniqueNameMovie;
import com.javaacademy.cinema.service.MovieService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @Value("${header.user-token.value}")
  private String tokenValue;

  @PostMapping
  public ResponseEntity<?> saveMovie(
      @RequestHeader("user-token") String token,
      @RequestBody MovieDto movieDto) {
    if (Objects.isNull(token) || !token.equals(tokenValue)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(movieDto));
    } catch (NotUniqueNameMovie ex) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }
  }

  @GetMapping
  public List<MovieDto> getAllMovies() {
    return movieService.getAllMovies();
  }
}
