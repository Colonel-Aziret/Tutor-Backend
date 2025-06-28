package com.example.tutor.model.user.request;

import com.example.tutor.constraint.AdminChangePassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@AdminChangePassword
public class AdminChangePasswordRequestDto {

    @Schema(description = "Почта пользователя")
    String email;

    String password;
}
