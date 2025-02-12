package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.exception.NotUniqueNameMovie;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

  private static final String SQL_QUERY_GET_MOVIE_BY_ID = "select * from movie where id = ?";
  private static final String SQL_QUERY_CREATE_MOVIE_AND_RETURN_ID =
      "insert into movie (name, description) values(?, ?) returning id";
  private static final String SQL_QUERY_GET_ALL_MOVIE = "select * from movie";
  private static final String SQL_QUERY_CHECK_UNIQUE_NAME_MOVIE =
      "select count(*) as cnt from movie where name = ?";

  private final JdbcTemplate jdbcTemplate;

  public Movie saveMovie(MovieDto movieDto) {
    checkUniqueNameMovie(movieDto.getName());
    Integer id = jdbcTemplate.queryForObject(
        SQL_QUERY_CREATE_MOVIE_AND_RETURN_ID,
        Integer.class,
        movieDto.getName(), movieDto.getDescription());
    return new Movie(id, movieDto.getName(), movieDto.getDescription());
  }

  public Optional<Movie> findMovieById(Integer id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              SQL_QUERY_GET_MOVIE_BY_ID,
              this::mapToMovie,
              id));
    } catch (
        EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public List<Movie> getAllMovies() {
    return jdbcTemplate.query(SQL_QUERY_GET_ALL_MOVIE, this::mapToMovie);
  }

  @SneakyThrows
  private Movie mapToMovie(ResultSet rs, int rowNum) {
    Movie movie = new Movie();
    movie.setId(rs.getInt("id"));
    movie.setName(rs.getString("name"));
    movie.setDescription(rs.getString("description"));
    return movie;
  }

  private void checkUniqueNameMovie(String name) {
    Integer count = jdbcTemplate.queryForObject(
        SQL_QUERY_CHECK_UNIQUE_NAME_MOVIE,
        Integer.class,
        name);
    if (Objects.nonNull(count) && count > 0) {
      throw new NotUniqueNameMovie(
          "Не удалось добавить фильм, фильм с таким названием уже есть в БД!");
    }
  }
}
