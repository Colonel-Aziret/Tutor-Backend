package com.example.tutor.service.hb;

import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.model.cities.CitiesFilterDto;
import com.example.tutor.model.cities.CitiesRequestDto;
import com.example.tutor.model.cities.CitiesResponseDto;
import com.example.tutor.model.cities.PageCitiesResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface CitiesService {

    HBCities findById(Long id);

    CitiesResponseDto create(CitiesRequestDto dto, HttpServletRequest request);

    CitiesResponseDto update(CitiesRequestDto dto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    PageCitiesResponseDto getAll(CitiesFilterDto filter);
}
