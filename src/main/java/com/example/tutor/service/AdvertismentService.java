package com.example.tutor.service;

import com.example.tutor.model.advertisment.AdvertismentRequestDto;
import com.example.tutor.model.advertisment.AdvertismentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertismentService {
    AdvertismentResponseDto create(AdvertismentRequestDto dto, List<MultipartFile> files, HttpServletRequest request);
    AdvertismentResponseDto findById(Long id);
    List<AdvertismentResponseDto> getAll();
}
