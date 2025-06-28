package com.example.tutor.service;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.model.tutorDetails.PageTutorReviewResponseDto;
import com.example.tutor.model.tutorDetails.TutorReviewFilterDto;
import com.example.tutor.model.tutorDetails.TutorReviewRequestDto;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.model.user.filter.UserFilterDto;
import com.example.tutor.model.user.request.AdminChangePasswordRequestDto;
import com.example.tutor.model.user.request.ResetPasswordRequestDto;
import com.example.tutor.model.user.request.SysUserChangePasswordRequestDto;
import com.example.tutor.model.user.request.SysUserRequest;
import com.example.tutor.model.user.response.PageSysUserDtoResponse;
import com.example.tutor.model.user.response.SysUserResponseDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.UUID;


public interface SysUserService {


    Date getStartOfDay();

    PageSysUserDtoResponse findAll(UserFilterDto filter);

    SysUser findById(Long id);

    SysUserResponseDto getById(Long id);

    SysUserResponseDto create(SysUserRequest userDto, HttpServletRequest request) throws Exception;

    SysUserResponseDto createUserByAdmin(SysUserRequest userDto, HttpServletRequest request) throws Exception;

    SysUserResponseDto update(SysUserRequest userDto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    SysUserResponseDto changePassword(SysUserChangePasswordRequestDto userDto, HttpServletRequest request);

    SysUserResponseDto adminChangePassword(AdminChangePasswordRequestDto changePasswordRequestDto, HttpServletRequest request);

    SysUserResponseDto changePasswordCurrentUser(SysUserChangePasswordRequestDto dto, HttpServletRequest request);

    SysUserResponseDto getByJWT(HttpServletRequest request);

    SysUser ban(Long userId, HttpServletRequest request);

    SysUser unban(Long userId, HttpServletRequest request);

    void updateTheNumberOfFailedLogins(String pin);

    SysUserResponseDto activateUser(UUID activationCode);

    void sendActivationLink(Long userId, HttpServletRequest request) throws MessagingException;

    void processForgotPassword(String email) throws MessagingException;

    void validateToken(String token);

    void resetPassword(ResetPasswordRequestDto requestDto);

    PhotoResponseDto uploadPhoto(MultipartFile file, HttpServletRequest request);

    PhotoResponseDto getPhoto(Long userId);


    SysUser getFromContext();
}
