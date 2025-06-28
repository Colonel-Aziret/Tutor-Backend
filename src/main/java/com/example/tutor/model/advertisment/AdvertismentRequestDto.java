package com.example.tutor.model.advertisment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "DTO для создания / обновления рекламы")
public class AdvertismentRequestDto {

    @Schema(description = "Описание рекламы", example = "Сдаётся квартира в центре города")
    String description;

}
