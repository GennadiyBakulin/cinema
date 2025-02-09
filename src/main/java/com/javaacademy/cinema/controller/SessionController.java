package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
import com.javaacademy.cinema.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@Tag(name = "Session Controller",
    description = "API для работы с сеансами.")
public class SessionController {

  private final SessionService sessionService;

  @Operation(
      summary = "Сохранение нового сеанса.",
      description = "Сохраняет в БД новый сеанс пользователем с ролью администратор.")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "Успешное сохранение сеанса",
              content = {
                  @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Session.class))
              }
          ),
          @ApiResponse(
              responseCode = "304",
              description = "Отмена операции в случае если не найден фильм с указанным Id",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          )
      }
  )
  @PostMapping
  public ResponseEntity<?> saveSession(@RequestBody SessionDtoRq sessionDtoRq) {
    try {
      Session session = sessionService.saveSession(sessionDtoRq);
      return ResponseEntity.status(HttpStatus.CREATED).body(session);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }
  }

  @Operation(
      summary = "Получение всех сеансов.",
      description = "Получение всех сеансов из БД пользователем с ролью администратор.")
  @ApiResponse(
      responseCode = "200",
      description = "Успешное получение всех сеансов",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = SessionDtoRs.class))
          )
      }
  )
  @GetMapping
  public List<SessionDtoRs> getAllSession() {
    return sessionService.getAllSession();
  }

  @Operation(
      summary = "Получение всех свободных мест на сеанс.",
      description = "Получение всех свободных мест из БД посетителем кинотеатра.")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "Успешное получение свободных мест на сеанс",
              content = {
                  @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      array = @ArraySchema(schema = @Schema(implementation = String.class))
                  )
              }
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Отмена операции в случае если не найден сеанс с указанным Id",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          )
      }
  )
  @GetMapping("/{id}/free-place")
  public ResponseEntity<?> getFreePlacesOnSession(@PathVariable Integer id) {
    try {
      return ResponseEntity.ok(sessionService.getFreePlaceOnSession(id));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }
}
