package com.javaacademy.cinema.it.controller;

import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.entity.dto.SessionDtoRq;
import com.javaacademy.cinema.entity.dto.SessionDtoRs;
import com.javaacademy.cinema.service.MovieService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Sql("/scripts/clean_table.sql")
class SessionControllerTest {

  @Autowired
  private MovieService movieService;

  private final RequestSpecification requestSpecification = new RequestSpecBuilder()
      .setBasePath("/session")
      .log(LogDetail.ALL)
      .build()
      .contentType("application/json");

  private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
      .log(LogDetail.ALL)
      .build();

  @Test
  @DisplayName("Успешное сохранение сеанса в БД")
  public void successSaveSession() {
    MovieDto movie = new MovieDto("Тест", "Описание фильма");
    Integer movieId = movieService.saveMovie(movie).getId();
    LocalDateTime dateTime = LocalDateTime.now();
    BigDecimal price = BigDecimal.valueOf(500);
    SessionDtoRq sessionRq = new SessionDtoRq(movieId, dateTime, price);

    Session session = RestAssured.given(requestSpecification)
        .body(sessionRq)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(201)
        .extract()
        .body()
        .as(Session.class);

    Assertions.assertTrue(Objects.nonNull(session.getId()));
    Assertions.assertEquals(dateTime, session.getDateTime());
    Assertions.assertEquals(price, session.getPrice());
  }

  @Test
  @DisplayName("Отмена сохранения сеанса в БД при не верно указанном id фильма")
  public void notSuccessSaveSessionIfIncorrectIdMovie() {
    Integer movieId = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    BigDecimal price = BigDecimal.valueOf(500);
    SessionDtoRq sessionRq = new SessionDtoRq(movieId, dateTime, price);

    RestAssured.given(requestSpecification)
        .body(sessionRq)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(404);
  }

  @Test
  @Sql({"/scripts/clean_table.sql", "/scripts/movie/add_list_movie.sql",
      "/scripts/session/add_list_session.sql"})
  @DisplayName("Успешное получение списка всех сеансов из БД")
  public void successGetListAllSession() {
    int expectCountSessionToDb = 4;

    List<SessionDtoRs> sessionList = RestAssured.given(requestSpecification)
        .get()
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertFalse(sessionList.isEmpty());
    Assertions.assertEquals(expectCountSessionToDb, sessionList.size());
  }

  @Test
  @DisplayName("Успешное получение пустого списка сеансов при их отсутствии в БД")
  public void successGetEmptyListSessions() {
    List<SessionDtoRs> sessionList = RestAssured.given(requestSpecification)
        .get()
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertTrue(sessionList.isEmpty());
  }

  @Test
  @Sql({"/scripts/clean_table.sql", "/scripts/fill-data.sql", "/scripts/movie/add_list_movie.sql",
      "/scripts/session/add_list_session.sql", "/scripts/ticket/add_list_ticket.sql"})
  @DisplayName("Успешное получение свободных мест на сеанс по id сеанса")
  public void SuccessGetFreePlaceOnSession() {
    Integer sessionId = 1;
    int expectCountFreePlace = 4;

    List<String> result = RestAssured.given(requestSpecification)
        .pathParam("id", sessionId)
        .get("/{id}/free-place")
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertEquals(expectCountFreePlace, result.size());
  }

  @Test
  @DisplayName("Ошибка получения свободных мест на сеанс при не верно указанном id сеанса")
  public void notSuccessGetFreePlaceIfIncorrectIdSession() {
    Integer sessionId = 1;

    RestAssured.given(requestSpecification)
        .pathParam("id", sessionId)
        .get("/{id}/free-place")
        .then()
        .spec(responseSpecification)
        .statusCode(404);
  }
}
