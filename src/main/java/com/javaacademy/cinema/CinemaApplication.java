package com.javaacademy.cinema;

import com.javaacademy.cinema.repository.MovieRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CinemaApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class, args);
    MovieRepository movieRepository = context.getBean(MovieRepository.class);
    movieRepository.findMovieById(1);
  }

}
