package com.javaacademy.cinema.it.controller;

import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.entity.dto.MovieSaveDtoRs;
import com.javaacademy.cinema.service.MovieService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
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
@Sql("/scripts/clean_table.sql")
class MovieControllerTest {

  @Autowired
  private MovieService movieService;

  private final Header token = new Header("user-token", "secretadmin123");
  private final Header tokenError = new Header("hacker", "error");
  private final MovieDto movieRequest = new MovieDto("Тест", "Описание фильма");

  private final RequestSpecification requestSpecification = new RequestSpecBuilder()
      .setBasePath("/movie")
      .log(LogDetail.ALL)
      .build()
      .contentType("application/json");

  private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
      .log(LogDetail.ALL)
      .build();

  @Test
  @DisplayName("Успешное сохранение фильма в БД")
  public void successSaveMovie() {
    MovieSaveDtoRs movie = RestAssured.given(requestSpecification)
        .header(token)
        .body(movieRequest)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(201)
        .extract()
        .body()
        .as(MovieSaveDtoRs.class);

    Assertions.assertTrue(Objects.nonNull(movie.getId()));
    Assertions.assertEquals(movieRequest.getName(), movie.getName());
    Assertions.assertEquals(movieRequest.getDescription(), movie.getDescription());
  }

  @Test
  @DisplayName("Запрещено сохранение фильма в БД при не верно указанном токене")
  public void notSuccessSaveMovieIfIncorrectToken() {
    RestAssured.given(requestSpecification)
        .header(tokenError)
        .body(movieRequest)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(403);
  }

  @Test
  @DisplayName("Запрещено сохранение фильма в БД при не указанном токене")
  public void notSuccessSaveMovieIfTokenNotSpecified() {
    RestAssured.given(requestSpecification)
        .body(movieRequest)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(403);
  }

  @Test
  @DisplayName("Успешное выбрасывание ошибки при попытке сохранения фильма с именем которое уже есть в БД")
  public void notSuccessSaveMovieIfExistName() {
    movieService.saveMovie(movieRequest);

    RestAssured.given(requestSpecification)
        .header(token)
        .body(movieRequest)
        .post()
        .then()
        .spec(responseSpecification)
        .statusCode(409);
  }

  @Test
  @Sql({"/scripts/clean_table.sql", "/scripts/movie/add_list_movie.sql"})
  @DisplayName("Успешное получение списка фильмов из БД")
  public void successGetListAllMovies() {
    int expectCountMoviesToDb = 4;

    List<MovieDto> movieList = RestAssured.given(requestSpecification)
        .get()
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertFalse(movieList.isEmpty());
    Assertions.assertEquals(expectCountMoviesToDb, movieList.size());
  }

  @Test
  @DisplayName("Успешное получение пустого списка фильмов при их отсутствии в БД")
  public void successGetEmptyListMovies() {
    List<MovieDto> movieList = RestAssured.given(requestSpecification)
        .get()
        .then()
        .spec(responseSpecification)
        .statusCode(200)
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    Assertions.assertTrue(movieList.isEmpty());
  }
}
