package com.example.tutor.service.hb;

import com.example.tutor.db.entity.HB.HBSubjects;

import com.example.tutor.model.subjects.PageSubjectsResponseDto;
import com.example.tutor.model.subjects.SubjectsFilterDto;
import com.example.tutor.model.subjects.SubjectsRequestDto;
import com.example.tutor.model.subjects.SubjectsResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface SubjectsService {
    HBSubjects findById(Long id);

    SubjectsResponseDto create(SubjectsRequestDto dto, HttpServletRequest request);

    SubjectsResponseDto update(SubjectsRequestDto dto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    PageSubjectsResponseDto getAll(SubjectsFilterDto filter);
}
