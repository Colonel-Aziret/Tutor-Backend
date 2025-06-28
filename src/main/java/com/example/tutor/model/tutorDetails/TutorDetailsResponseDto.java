package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.sys.TutorDetails;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;


@Data
@Builder
public class TutorDetailsResponseDto {
    Double price;
    String aboutMe;
    Boolean online;
    Boolean offline;
    Boolean atTutor;
    String address;
    Long cityId;
    String cityName;
    @Schema(description = "Средняя оценка тьютора")
    Double rate;
    String telegram;
    Integer experience;

    @Schema(description = "Отзывы")
    List<TutorReviewResponseDto> reviews;

    public static TutorDetailsResponseDto from(TutorDetails td, FileUtils fileUtils) {
        return TutorDetailsResponseDto.builder()
                .rate(td.getRate())
                .reviews(td.getReviews() != null
                        ? td.getReviews().stream()
                        .map(r -> TutorReviewResponseDto.from(r, fileUtils))
                        .toList()
                        : Collections.emptyList())
                .price(td.getPrice())
                .telegram(td.getTelegram())
                .experience(td.getExperience())
                .aboutMe(td.getAboutMe())
                .online(td.getOnline())
                .offline(td.getOffline())
                .atTutor(td.getAtTutor())
                .address(td.getAddress())
                .cityId(td.getCity() != null ? td.getCity().getId() : null)
                .cityName(td.getCity() != null ? td.getCity().getNameRu() : null)
                .build();
    }
}
