package com.example.tutor.model.actionLog.response;

import com.example.tutor.db.entity.ActionLog;
import com.example.tutor.db.enums.HttpMethodType;
import com.example.tutor.db.enums.ProcessObjectType;
import com.example.tutor.model.user.response.SysUserResponseDto;
import com.example.tutor.util.FileUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@Schema(description = "Модель логирования журнала")
public class ActionLogResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    Long id;

    @Schema(description = "Пользователь", implementation = SysUserResponseDto.class)
    SysUserResponseDto user;

    @Schema(description = "IP адресс пользователя", example = "0.0.0.0.0")
    String remoteIp;

    @Schema(description = "GSON объекта", example = "{}")
    String details;

    @Schema(description = "Класс действия", example = "APPLICATION", implementation = ProcessObjectType.class)
    ProcessObjectType objectType;

    @Schema(description = "Тип действия", example = "GET", implementation = HttpMethodType.class)
    HttpMethodType methodType;

    @Schema(description = "Время записи в журнал", example = "2024-01-01 10:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdTime;

    public static ActionLogResponse from(ActionLog log) {
        return ActionLogResponse.builder()
                .id(log.getId())
                .user(SysUserResponseDto.from(log.getUser(), new FileUtils()))
                .remoteIp(log.getRemoteIp())
                .details(log.getDetails())
                .objectType(log.getObjectType())
                .methodType(log.getHttpMethodType())
                .createdTime(log.getCreatedTime())
                .build();
    }


}
