package com.example.tutor.service.impl;

import com.example.tutor.db.entity.HB.Advertisment;
import com.example.tutor.db.entity.HB.AdvertismentPhoto;
import com.example.tutor.db.repository.AdvertismentPhotoRepository;
import com.example.tutor.db.repository.AdvertismentRepository;
import com.example.tutor.exception.NotFoundException;
import com.example.tutor.model.advertisment.AdvertismentRequestDto;
import com.example.tutor.model.advertisment.AdvertismentResponseDto;
import com.example.tutor.service.AdvertismentService;
import com.example.tutor.service.SysLogRequestService;
import com.example.tutor.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertismentServiceImpl implements AdvertismentService {

    private final AdvertismentRepository advertismentRepository;
    private final AdvertismentPhotoRepository advertismentPhotoRepository;
    private final FileUtils fileStorageService;
    private final SysLogRequestService logService;

    @Override
    @Transactional
    public AdvertismentResponseDto create(AdvertismentRequestDto dto, List<MultipartFile> files, HttpServletRequest request) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("error.photo_required");
        }

        Advertisment advertisment = Advertisment.builder()
                .description(dto.getDescription())
                .build();

        advertisment = advertismentRepository.save(advertisment);

        List<AdvertismentPhoto> savedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String savedPath = fileStorageService.saveMultipartFileWithResize(file);

            AdvertismentPhoto photo = AdvertismentPhoto.builder()
                    .advertisment(advertisment)
                    .filePath(savedPath)
                    .filePathThumb("mini/" + savedPath)
                    .filePathLarge("large/" + savedPath)
                    .contentType(file.getContentType())
                    .build();

            savedPhotos.add(advertismentPhotoRepository.save(photo));
        }

        advertisment.setPhotos(savedPhotos);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                String.format("Created advertisment with %d photos", savedPhotos.size()),
                request
        );

        return AdvertismentResponseDto.from(advertisment);
    }

    @Override
    public AdvertismentResponseDto findById(Long id) {
        Advertisment advertisment = advertismentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.advertisment.not_found"));
        return AdvertismentResponseDto.from(advertisment);
    }

    @Override
    public List<AdvertismentResponseDto> getAll() {
        return advertismentRepository.findAll().stream()
                .map(AdvertismentResponseDto::from)
                .collect(Collectors.toList());
    }

}
