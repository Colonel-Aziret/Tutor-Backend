package com.example.tutor.model.user.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.model.BasePageRequest;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO для фильтрации пользователей")
public class UserFilterDto extends BasePageRequest {

    @Schema(description = "Фильтрация по роли TUTOR: true — только туторы, false — только не туторы, null — все", example = "true")
    Boolean isTutor;

    @Schema(description = "Сортировка по рейтингу: ASC — по возрастанию, DESC — по убыванию")
    String sortByRate;

    @Schema(description = "Полное имя пользователя", example = "Иванов Иван Иванович")
    String fullName;

    @Schema(description = "Номер телефона", example = "+996123456789")
    String phoneNumber;

    @Schema(description = "Список идентификаторов ролей", example = "[1, 2, 3]")
    List<Long> rolesIds;

    @Schema(description = "Удален?", example = "false")
    Boolean deleted = false;

    @Schema(description = "Заблокирован?", example = "false")
    Boolean isBanned = false;

    Double minRate;
    Double maxRate;
    Double minPrice;
    Double maxPrice;
    List<Integer> cityIds;
    List<Integer> subjectIds;
    Boolean online;
    Boolean offline;
    Boolean atTutor;
    String telegram;
    Integer experience;

}
