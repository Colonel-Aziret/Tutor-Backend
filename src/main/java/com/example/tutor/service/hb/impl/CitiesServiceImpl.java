package com.example.tutor.service.hb.impl;


import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.db.repository.hb.CitiesRepository;
import com.example.tutor.db.repository.specification.CitiesSpecification;
import com.example.tutor.exception.NotFoundException;
import com.example.tutor.model.cities.CitiesFilterDto;
import com.example.tutor.model.cities.CitiesRequestDto;
import com.example.tutor.model.cities.CitiesResponseDto;
import com.example.tutor.model.cities.PageCitiesResponseDto;
import com.example.tutor.service.SysLogRequestService;
import com.example.tutor.service.hb.CitiesService;
import com.example.tutor.util.GsonConfig;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository repository;
    private final SysLogRequestService logService;
    private final Gson gson = GsonConfig.createGson();

    @Override
    public HBCities findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.city.not_found"));
    }

    @Override
    public CitiesResponseDto create(CitiesRequestDto dto, HttpServletRequest request) {
        Optional<HBCities> byAlias = repository.findByAlias(dto.getAlias());
        HBCities city;
        if (byAlias.isPresent()) {
            city = byAlias.get();
            city.setDeleted(false);
            city.setEditedTime(new Date());
        } else {
            city = HBCities.builder()
                    .alias(dto.getAlias())
                    .nameRu(dto.getNameRu())
                    .nameKy(dto.getNameKy())
                    .build();
        }

        var result = repository.save(city);
        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(dto), request);

        return CitiesResponseDto.from(result);
    }

    @Override
    public CitiesResponseDto update(CitiesRequestDto dto, HttpServletRequest request) {
        HBCities city = findById(dto.getId());

        city.setAlias(dto.getAlias());
        city.setNameRu(dto.getNameRu());
        city.setNameKy(dto.getNameKy());
        city.setDeleted(false);
        city.setEditedTime(new Date());

        var result = repository.save(city);
        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(dto), request);

        return CitiesResponseDto.from(result);
    }

    @Override
    public void delete(Long id, HttpServletRequest request) {
        var city = findById(id);
        city.setDeleted(true);
        repository.save(city);

        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(id), request);
    }

    @Override
    public PageCitiesResponseDto getAll(CitiesFilterDto filter) {
        CitiesSpecification specification = new CitiesSpecification(filter);
        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );
        Page<HBCities> page = repository.findAll(specification, pageable);
        return PageCitiesResponseDto.from(page);
    }
}
