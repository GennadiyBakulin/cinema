package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

  private final PlaceRepository placeRepository;

  public Place findPlaceById(Integer id) {
    return placeRepository.findPlaceById(id).orElseThrow();
  }

  public List<Place> getAllPlace() {
    return placeRepository.getAllPlace();
  }
}
