package com.example.tutor.model.user;

import com.example.tutor.db.entity.HB.UserPhoto;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Фото услуги")
public class PhotoResponseDto {

    @Schema(description = "ID фото")
    Long id;

    @Schema(description = "URL миниатюры (170x126)")
    String urlMini;

    @Schema(description = "URL стандартного изображения")
    String urlStandard;

    @Schema(description = "URL увеличенного изображения (420x380)")
    String urlLarge;

    public static PhotoResponseDto from(UserPhoto photo, FileUtils fileUtils) {
        return PhotoResponseDto.builder()
                .id(photo.getId())
                .urlMini(fileUtils.buildUrl(photo.getFilePathThumb()))
                .urlStandard(fileUtils.buildUrl(photo.getFilePath()))
                .urlLarge(fileUtils.buildUrl(photo.getFilePathLarge()))
                .build();
    }
}
