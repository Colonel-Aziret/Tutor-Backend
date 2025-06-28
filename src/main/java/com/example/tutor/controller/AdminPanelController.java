package com.example.tutor.controller;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.repository.ErrorMessageRepository;
import com.example.tutor.model.AuthenticationRequest;
import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.errorMessage.ErrorDto;
import com.example.tutor.model.user.request.AdminChangePasswordRequestDto;
import com.example.tutor.model.user.response.SysUserResponseDto;
import com.example.tutor.security.JwtUtil;
import com.example.tutor.service.ReviewService;
import com.example.tutor.service.SysLogAuthorizationService;
import com.example.tutor.service.SysLogRequestService;
import com.example.tutor.service.SysUserService;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RestController
@Setter
@RequestMapping("/admin-panel")
@RequiredArgsConstructor
@Tag(name = "Админ панель", description = "Администрирование")
public class AdminPanelController {

    private final BaseController baseController;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final SysLogAuthorizationService logsAuthorizationService;
    private final ErrorMessageRepository errorMessageRepository;
    private final SysUserService sysUserService;
    private final SysLogRequestService sysLogRequestService;
    private final FileUtils fileUtils;
    private final ReviewService reviewService;


    @Value("${password.duration}")
    private String passwordDuration;


    @PostMapping("/change-password")
    @Operation(summary = "Обновление пароля пользователя Супер Администратором",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для смены пароля пользователя",
                    content = @Content(schema = @Schema(implementation = AdminChangePasswordRequestDto.class))
            ),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> changePasswordAnotherUser(
            @Validated @RequestBody AdminChangePasswordRequestDto changePasswordRequestDto,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(sysUserService.adminChangePassword(changePasswordRequestDto, request))
                        .build(), HttpStatus.OK
        );

    }


    @PutMapping("/comment-ban/{userId}")
    @Operation(
            summary = "Запретить пользователю оставлять комментарии и удалить все его отзывы",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt")
                    )
            }
    )
    public ResponseEntity<BaseResponse> banUserComments(
            @Parameter(description = "ID автора", required = true) @PathVariable Long userId) {
        reviewService.banUserFromComments(userId);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg("Пользователь заблокирован для комментариев и все отзывы удалены")
                        .res(userId)
                        .build()
        );
    }

    @PutMapping("/comment-unban/{userId}")
    @Operation(
            summary = "Разрешить пользователю снова оставлять комментарии",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt")
                    )
            }
    )
    public ResponseEntity<BaseResponse> unbanUserComments(@Parameter(description = "ID автора", required = true) @PathVariable Long userId) {
        reviewService.unbanUserFromComments(userId);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .msg("Пользователю разрешено снова оставлять комментарии")
                        .res(userId)
                        .build()
        );
    }

    @PostMapping("/activation-link/{userId}")
    @Operation(summary = "Повторно отправить ссылку с активацией аккаунта",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt"))}
    )
    public ResponseEntity<BaseResponse> sendActivationLink(@PathVariable Long userId, HttpServletRequest request) throws MessagingException {
        sysUserService.sendActivationLink(userId, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg("The letter with the link for activation was successfully sent")
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/ban/{userId}")
    @Operation(summary = "Блокировка пользователя",
            responses = @ApiResponse(description = "Закрывает доступ пользователю в систему", content =
            @Content(schema =
            @Schema(implementation = SysUserResponseDto.class))),
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt"))}
    )
    public ResponseEntity<BaseResponse> ban(@PathVariable Long userId, HttpServletRequest request) {
        SysUser user = sysUserService.ban(userId, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(SysUserResponseDto.from(user, fileUtils))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/unban/{userId}")
    @Operation(summary = "Разблокировка пользователя",
            responses = @ApiResponse(description = "Возвращает доступ пользователю в систему", content =
            @Content(schema =
            @Schema(implementation = SysUserResponseDto.class))),
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt"))}
    )
    public ResponseEntity<BaseResponse> unban(@PathVariable Long userId, HttpServletRequest request) {
        SysUser user = sysUserService.unban(userId, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(SysUserResponseDto.from(user, fileUtils))
                        .build(),
                HttpStatus.OK
        );
    }


    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя",
            responses = @ApiResponse(description = "Возвращает JWT токен для последующих запросов", content =
            @Content(schema =
            @Schema(implementation = String.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для аутентификации",
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class))
            )
    )
    public ResponseEntity<BaseResponse> login(
            @Validated @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletRequest request) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            sysLogRequestService.saveErrorToFileAndDb(
                    this.getClass().getSimpleName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    String.format(
                            "Login error - username: %s",
                            authenticationRequest.getUsername()),
                    request);
            sysUserService.updateTheNumberOfFailedLogins(authenticationRequest.getUsername());
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.ERROR)
                            .msg(ErrorDto.from(errorMessageRepository.findById("error.auth").get()))
                            .res(null)
                            .build(),
                    HttpStatus.UNAUTHORIZED
            );
        }

        Optional<SysUser> userFromToken =
                baseController.userRepository.findByEmail(authenticationRequest.getUsername());
        if (userFromToken.isPresent()) {
            SysUser user = userFromToken.get();

//            if (!user.isEmailVerified()) {
//                return accountNotActivated();
//            }

            if (user.isBanned() || (user.getTemporaryAccessUntilTime() != null && user.getTemporaryAccessUntilTime().before(new Date())))
                return accessDenied();

            if (!user.getPasswordChangeNextLogon() && user.getPasswordLastChangeTime() != null && isPasswordExpired(user.getPasswordLastChangeTime())) {
                user.setPasswordChangeNextLogon(true);
            }

            user.setLastLogin(new Date());
            user.setFailedLoginAttempts(0);
            baseController.userRepository.save(user);// Save the updated user to the database
            logsAuthorizationService.saveSuccessfulAuth(user, baseController.tryDetectRemoteClientIp(request));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(jwtUtil.generateToken(userDetails.getUsername()))
                        .build(),
                HttpStatus.OK
        );
    }

    private ResponseEntity<BaseResponse> accountNotActivated() {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.ERROR)
                        .msg("The account has not yet been activated")
                        .res(ErrorDto.from(errorMessageRepository.findById("error.access.not_activated").get()))
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    private ResponseEntity<BaseResponse> accessDenied() {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.ERROR)
                        .msg("Access timed out")
                        .res(ErrorDto.from(errorMessageRepository.findById("error.access.timed_out").get()))
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    private boolean isPasswordExpired(Date passwordLastChangeTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(passwordLastChangeTime);

        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(passwordDuration));
        Date today = new Date();
        return calendar.getTime().before(today);
    }

    private boolean hasNoSystemRole(SysUser user) {
        return user.getRoles().stream()
                .noneMatch(role -> role.getAlias().equalsIgnoreCase("superadministrator"));
    }
}
