package com.example.tutor.model.advertisment;

import com.example.tutor.db.entity.HB.AdvertismentPhoto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AdvertismentPhotoResponseDto {
    Long id;
    String filePath;
    String filePathThumb;
    String filePathLarge;
    String contentType;
    Date createdTime;

    public static AdvertismentPhotoResponseDto from(AdvertismentPhoto entity) {
        return AdvertismentPhotoResponseDto.builder()
                .id(entity.getId())
                .filePath(entity.getFilePath())
                .filePathThumb(entity.getFilePathThumb())
                .filePathLarge(entity.getFilePathLarge())
                .contentType(entity.getContentType())
                .createdTime(entity.getCreatedTime())
                .build();
    }
}
