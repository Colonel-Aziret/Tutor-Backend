package com.example.tutor.model.user.request;

import com.example.tutor.model.tutorDetails.SubjectExperienceRequestDto;
import com.example.tutor.model.tutorDetails.TutorDetailsRequestDto;
import com.example.tutor.model.tutorDetails.TutorEducationRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import com.example.tutor.constraint.FutureDate;
import com.example.tutor.constraint.ValidUser;
import com.example.tutor.db.enums.Gender;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "Модель создания пользователя")
@ValidUser
public class SysUserRequest {

    @Schema(description = "Идентификатор пользователя")
    Long id;

    @NotBlank(message = "error.valid.last.name.not_null")
    String secondName;

    @NotBlank(message = "error.valid.name.not_null")
    String name;

    @Schema(description = "Пол", implementation = Gender.class)
    Gender gender;

    @Schema(description = "Пароль пользователя", example = "Password123!")
    String password;

    @Schema(description = "Дата рождения")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    Date birthdate;

    @FutureDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date temporaryAccessUntilTime;

    @Schema(description = "Список идентификаторов ролей", example = "[1, 2]")
    List<Long> roleIds;

    @Schema(description = "Адрес электронной почты пользователя", example = "ivanov@example.com")
    String email;

    @Schema(description = "Ник в телеграм")
    String telegram;

    @Schema(description = "Номер телефона пользователя", example = "+996111222333")
    String phone;

    @Schema(description = "Tutor Details, если пользователь — Тьютор")
    TutorDetailsRequestDto tutorDetails;

    @Schema(description = "Образование Тьютора")
    List<TutorEducationRequestDto> tutorEducations;

    @Schema(description = "Список предметов, которые преподаёт тьютор")
    List<SubjectExperienceRequestDto> subjectExperiences;

    @Schema(description = "Возраст", example = "18")
    Integer age;

}
