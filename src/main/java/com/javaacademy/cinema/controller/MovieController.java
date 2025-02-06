package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.exception.NotUniqueNameMovie;
import com.javaacademy.cinema.repository.MovieRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

  private final MovieRepository repository;

  @PostMapping
  public ResponseEntity<?> saveMovie(
      @RequestHeader("user-token") String token,
      @RequestBody MovieDto movieDto) {
    if (Objects.isNull(token) || !token.equals("secretadmin123")) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(repository.saveMovie(movieDto));
    } catch (NotUniqueNameMovie ex) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }
  }
}
