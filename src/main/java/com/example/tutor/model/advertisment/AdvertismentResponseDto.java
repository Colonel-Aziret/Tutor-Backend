package com.example.tutor.model.advertisment;

import com.example.tutor.db.entity.HB.Advertisment;
import com.example.tutor.db.entity.HB.AdvertismentPhoto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class AdvertismentResponseDto {
    Long id;
    String description;
    Date createdTime;
    List<AdvertismentPhotoResponseDto> photos;

    public static AdvertismentResponseDto from(Advertisment entity) {
        return AdvertismentResponseDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .createdTime(entity.getCreatedTime())
                .photos(entity.getPhotos() != null
                        ? entity.getPhotos().stream()
                        .map(AdvertismentPhotoResponseDto::from)
                        .toList()
                        : List.of())
                .build();
    }
}

