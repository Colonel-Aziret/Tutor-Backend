package com.example.tutor.service.hb.impl;

import com.example.tutor.controller.BaseController;

import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.db.repository.hb.SubjectsRepository;
import com.example.tutor.db.repository.specification.SubjectsSpecification;
import com.example.tutor.exception.NotFoundException;

import com.example.tutor.model.subjects.PageSubjectsResponseDto;
import com.example.tutor.model.subjects.SubjectsFilterDto;
import com.example.tutor.model.subjects.SubjectsRequestDto;
import com.example.tutor.model.subjects.SubjectsResponseDto;
import com.example.tutor.service.SysLogRequestService;
import com.example.tutor.service.hb.SubjectsService;
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
public class SubjectsServiceImpl implements SubjectsService {
    private final SubjectsRepository repository;
    private final SysLogRequestService logService;
    private final Gson gson = GsonConfig.createGson();

    @Override
    public HBSubjects findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.subject.not_found"));
    }

    @Override
    public SubjectsResponseDto create(SubjectsRequestDto dto, HttpServletRequest request) {
        Optional<HBSubjects> byAlias = repository.findByAlias(dto.getAlias());
        HBSubjects city;
        if (byAlias.isPresent()) {
            city = byAlias.get();
            city.setDeleted(false);
            city.setEditedTime(new Date());
        } else {
            city = HBSubjects.builder()
                    .alias(dto.getAlias())
                    .nameRu(dto.getNameRu())
                    .nameKy(dto.getNameKy())
                    .build();
        }

        var result = repository.save(city);
        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(dto), request);

        return SubjectsResponseDto.from(result);
    }

    @Override
    public SubjectsResponseDto update(SubjectsRequestDto dto, HttpServletRequest request) {
        HBSubjects city = findById(dto.getId());

        city.setAlias(dto.getAlias());
        city.setNameRu(dto.getNameRu());
        city.setNameKy(dto.getNameKy());
        city.setDeleted(false);
        city.setEditedTime(new Date());

        var result = repository.save(city);
        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(dto), request);

        return SubjectsResponseDto.from(result);
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
    public PageSubjectsResponseDto getAll(SubjectsFilterDto filter) {
        SubjectsSpecification specification = new SubjectsSpecification(filter);
        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );
        Page<HBSubjects> page = repository.findAll(specification, pageable);
        return PageSubjectsResponseDto.from(page);
    }
}
