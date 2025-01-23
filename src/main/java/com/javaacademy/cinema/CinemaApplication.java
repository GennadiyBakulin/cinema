package com.javaacademy.cinema;

import com.javaacademy.cinema.repository.CinemaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CinemaApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class, args);
    CinemaRepository cinemaRepository = context.getBean(CinemaRepository.class);
    cinemaRepository.findMovieById(1);
  }

}
