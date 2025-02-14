package com.javaacademy.cinema.it.controller;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRq;
import com.javaacademy.cinema.entity.dto.TicketBookingDtoRs;
import com.javaacademy.cinema.entity.dto.TicketPurchasedDtoRs;
import com.javaacademy.cinema.service.SessionService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql({"/scripts/clean_table.sql", "/scripts/fill-data.sql", "/scripts/movie/add_list_movie.sql",
    "/scripts/session/add_list_session.sql", "/scripts/ticket/add_list_ticket.sql"})
class TicketControllerTest {

  @Autowired
  private SessionService sessionService;

  private final RequestSpecification requestSpecification = new RequestSpecBuilder()
      .setBasePath("/ticket")
      .log(LogDetail.ALL)
      .build()
      .contentType("application/json");

  private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
      .log(LogDetail.ALL)
      .build();

  @Test
  @DisplayName("Успешное получение списка проданных билетов на сеанс")
  public void successGetListPurchasedTicket() {
    Integer sessionId = 1;
    int expectCountPurchasedTicketToDb = 5;

    List<TicketPurchasedDtoRs> ticketList = RestAssured.given(requestSpecification)
        .queryParams("sessionId", sessionId)
        .get("/saled")
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertFalse(ticketList.isEmpty());
    Assertions.assertEquals(expectCountPurchasedTicketToDb, ticketList.size());
  }

  @Test
  @Sql("/scripts/clean_table.sql")
  @DisplayName("Ошибка получения списка проданных билетов на сеанс при не верно указанном id сеанса")
  public void notSuccessGetListPurchasedTicketIfIncorrectSessionId() {
    Integer sessionId = 10;

    RestAssured.given(requestSpecification)
        .queryParams("sessionId", sessionId)
        .get("/saled")
        .then()
        .spec(responseSpecification)
        .statusCode(404);
  }

  @Test
  @DisplayName("Успешная покупка билета на сеанс")
  public void successBookingTicket() {
    Integer sessionId = 1;
    TicketBookingDtoRq booking = new TicketBookingDtoRq(sessionId, "A1");
    Session session = sessionService.findSessionById(sessionId);

    TicketBookingDtoRs ticket = RestAssured.given(requestSpecification)
        .body(booking)
        .post("/booking")
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(TicketBookingDtoRs.class);

    Assertions.assertTrue(Objects.nonNull(ticket));
    Assertions.assertEquals(booking.getPlaceName(), ticket.getPlaceName());
    Assertions.assertEquals(session.getMovie().getName(), ticket.getMovieName());
    Assertions.assertEquals(session.getDateTime(), ticket.getDate());
  }

  @Test
  @DisplayName("Ошибка покупки уже проданного билета на сеанс")
  public void notSuccessBookingTicketIfTicketIsPurchased() {
    Integer sessionId = 1;
    TicketBookingDtoRq booking = new TicketBookingDtoRq(sessionId, "A5");

    RestAssured.given(requestSpecification)
        .body(booking)
        .post("/booking")
        .then()
        .spec(responseSpecification)
        .statusCode(409);
  }

  @Test
  @DisplayName("Ошибка покупки билета на сеанс если место указано неверно")
  public void notSuccessBookingTicketIfPlaceIncorrect() {
    Integer sessionId = 1;
    TicketBookingDtoRq booking = new TicketBookingDtoRq(sessionId, "K12");

    RestAssured.given(requestSpecification)
        .body(booking)
        .post("/booking")
        .then()
        .spec(responseSpecification)
        .statusCode(409);
  }

  @Test
  @DisplayName("Ошибка покупки билета на сеанс при не верно указанном id сеанса")
  public void notSuccessBookingTicketIfIncorrectSessionId() {
    Integer sessionId = 10;
    TicketBookingDtoRq booking = new TicketBookingDtoRq(sessionId, "A1");

    RestAssured.given(requestSpecification)
        .body(booking)
        .post("/booking")
        .then()
        .spec(responseSpecification)
        .statusCode(404);
  }
}
