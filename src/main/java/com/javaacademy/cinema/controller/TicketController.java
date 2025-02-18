package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.entity.dto.TicketBookingDtoRq;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRs;
import com.javaacademy.cinema.entity.dto.TicketPurchasedDtoRs;
import com.javaacademy.cinema.service.TicketService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "Ticket Controller",
    description = "API для работы с билетами.")
public class TicketController {

  private final TicketService ticketService;

  @Operation(
      summary = "Показывает список купленных билетов.",
      description = "Показывает список купленных билетов из БД пользователю с ролью администратор.")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Успешное получение проданных билетов на сеанс",
              content = {
                  @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      array = @ArraySchema(schema = @Schema(implementation = TicketPurchasedDtoRs.class))
                  )
              }
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Отмена операции в случае если не найден указанный сеанс",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          )})
  @GetMapping("/saled")
  @ResponseStatus(HttpStatus.OK)
  public List<TicketPurchasedDtoRs> getListPurchasedTicket(@RequestParam Integer sessionId) {
    return ticketService.getListPurchasedTicket(sessionId);
  }

  @Operation(
      summary = "Покупка билета на сеанс.",
      description = "Покупка билета на сеанс посетителем кинотеатра.")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Успешный выкуп билета",
              content = {
                  @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = TicketBookingDtoRs.class))
              }
          ),
          @ApiResponse(
              responseCode = "409",
              description = "Отмена операции в случае если билет уже был выкуплен или не найдено место",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Отмена операции в случае если не найден указанный сеанс",
              content = {
                  @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                      schema = @Schema(implementation = String.class))
              }
          )
      }
  )
  @PostMapping("/booking")
  @ResponseStatus(HttpStatus.OK)
  public TicketBookingDtoRs bookingTicket(@RequestBody TicketBookingDtoRq ticketBookingDtoRq) {
    return ticketService.bookingTicket(ticketBookingDtoRq);
  }
}
