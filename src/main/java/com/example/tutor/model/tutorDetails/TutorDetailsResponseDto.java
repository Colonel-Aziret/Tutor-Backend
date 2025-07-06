package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.TutorReview;
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
    Integer experience;

    @Schema(description = "Отзывы")
    List<TutorReviewResponseDto> reviews;

    public static TutorDetailsResponseDto from(TutorDetails td, FileUtils fileUtils) {
        List<TutorReview> allReviews = td.getReviews() != null ? td.getReviews() : Collections.emptyList();

        List<TutorReviewResponseDto> rootReviews = allReviews.stream()
                .filter(r -> r.getParent() == null)
                .map(r -> TutorReviewResponseDto.from(r, fileUtils, allReviews))
                .toList();

        return TutorDetailsResponseDto.builder()
                .rate(td.getRate())
                .reviews(rootReviews)
                .price(td.getPrice())
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
