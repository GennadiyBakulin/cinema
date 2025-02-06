package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.dto.MovieDto;
import com.javaacademy.cinema.mapper.MovieMapper;
import java.sql.ResultSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

  private static final String SQL_QUERY_GET_MOVIE_BY_ID = "select * from movie where id = ?";
  private static final String SQL_QUERY_CREATE_MOVIE_AND_RETURN_ID =
      "insert into movie (name, description) values(?, ?) returning id";

  private final JdbcTemplate jdbcTemplate;
  private final MovieMapper mapper;

  public Optional<Movie> findMovieById(Integer id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            SQL_QUERY_GET_MOVIE_BY_ID,
            this::mapToMovie,
            id));
  }

  @SneakyThrows
  private Movie mapToMovie(ResultSet rs, int rowNum) {
    Movie movie = new Movie();
    movie.setId(rs.getInt("id"));
    movie.setName(rs.getString("name"));
    movie.setDescription(rs.getString("description"));
    return movie;
  }

  public Movie movie(MovieDto movieDto) {
    Integer id = jdbcTemplate.queryForObject(
        SQL_QUERY_CREATE_MOVIE_AND_RETURN_ID,
        Integer.class,
        movieDto.getName(), movieDto.getDescription());
    return mapper.movieDtoToEntity(movieDto, id);
  }
}
