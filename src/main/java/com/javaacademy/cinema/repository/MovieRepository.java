package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.exception.NotUniqueNameMovie;
import com.javaacademy.cinema.mapper.MovieMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
  private final MovieMapper mapper;

  public Movie saveMovie(MovieDto movieDto) {
    checkUniqueNameMovie(movieDto.getName());
    Integer id = jdbcTemplate.queryForObject(
        SQL_QUERY_CREATE_MOVIE_AND_RETURN_ID,
        Integer.class,
        movieDto.getName(), movieDto.getDescription());
    return mapper.movieDtoToEntity(movieDto, id);
  }

  private void checkUniqueNameMovie(String name) {
    Integer count = jdbcTemplate.queryForObject(
        SQL_QUERY_CHECK_UNIQUE_NAME_MOVIE,
        new RowMapper<Integer>() {
          @Override
          public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getRow();
          }
        },
        name);
    if (count != 0) {
      throw new NotUniqueNameMovie(
          "Не удалось добавить фильм, фильм с таким названием уже есть в БД!");
    }
  }

  public Optional<Movie> findMovieById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_MOVIE_BY_ID,
            this::mapToMovie,
            id));
  }

  public List<Movie> getAllMovie() {
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
}
