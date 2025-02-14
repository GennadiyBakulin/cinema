package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.entity.dto.MovieSaveDtoRs;
import com.javaacademy.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@Tag(name = "Movie Controller",
    description = "API для работы с фильмами.")
public class MovieController {

  private final MovieService movieService;

  @Operation(
      summary = "Сохранение нового фильма.",
      description = "Проводит проверку наличия роли администратор у пользователя и в случае"
          + " успешной проверки сохраняет в БД новый фильм пользователем с ролью администратор.")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "Успешное сохранение нового фильма",
              content = {
                  @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Movie.class))
              }
          ),
          @ApiResponse(
              responseCode = "403",
              headers = @Header(
                  name = "user-token",
                  description = "Проверка секретного токена для роли администратор"
              ),
              description = "Отмена операции в случае отсутствия в заголовке запроса или несовпадения токена",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Не удалось добавить фильм, фильм с таким названием уже есть в БД!",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          )
      }
  )
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MovieSaveDtoRs saveMovie(
      @RequestHeader(value = "user-token", required = false) String token,
      @RequestBody MovieDto movieDto) {
    movieService.tokenValidate(token);
    return movieService.saveMovie(movieDto);
  }

  @Operation(
      summary = "Получение списка всех фильмов.",
      description = "Выдает из БД список всех фильмов посетителю кинотеатра.")
  @ApiResponse(
      responseCode = "200",
      description = "Успешное получение списка всех фильмов",
      content = {
          @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MovieDto.class))
          )
      }
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<MovieDto> getAllMovies() {
    return movieService.getAllMovies();
  }
}
