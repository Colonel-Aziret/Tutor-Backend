package com.example.tutor.controller;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.repository.SysUserRepository;
import com.example.tutor.exception.FTRuntimeException;
import com.example.tutor.model.BaseResponse;
import com.example.tutor.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseController {
    public final SysUserRepository userRepository;
    private final JwtUtil jwtUtil;

    public BaseController(SysUserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public static Integer getPage(Integer page) {
        if (page != null && page > 0) {
            return page - 1;
        }
        return 0;
    }

    public Optional<SysUser> getUserFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.BEARER)) {
            String substring = authorizationHeader.substring(Constants.BEARER.length());
            String username = jwtUtil.extractUsername(substring);
            return userRepository.findByEmail(username);
        } else {
            return Optional.empty();
        }
    }

    public SysUser getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return userRepository.findByEmail(username).get();
        }
        return null;
    }

    public ResponseEntity<BaseResponse> exception(
            Class<?> clazz,
            Exception e,
            String nameFunction
    ) {
        Log logger = LogFactory.getLog(clazz);

        if (!(e instanceof FTRuntimeException)) {
            logger.error(nameFunction + " exception: " + e.getMessage());
        }

        return new ResponseEntity<>(BaseResponse.builder()
                .success(Constants.ERROR)
                .msg(e.getMessage() != null ? e.getMessage() : Constants.UNDEFINED_FIELD)
                .res(null)
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    public String tryDetectRemoteClientIp(HttpServletRequest httpServletRequest) {
        try {
            String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
            return xForwardedFor != null ? xForwardedFor : httpServletRequest.getRemoteAddr();
        } catch (Exception ignored) {
        }
        return httpServletRequest.getRemoteAddr();
    }

    public static class Constants {
        public static final boolean SUCCESS = true;
        public static final boolean ERROR = false;
        public static final String BEARER = "Bearer ";
        public static final long UNDEFINED_ID_LONG = 0L;
        public static final int UNDEFINED_ID_INT = 0;
        public static final String UNDEFINED_FIELD = "";
        public static final Long COUNT_HOURS_ACCESS = 8L;
        public static final Long COUNT_HOURS_REFRESH = 18L;

        public enum RestResponseMessages {
            BAD_REQUEST, NOT_FOUND
        }

        public enum LOG_STATUS {
            ERROR, SUCCESS

        }
    }

}
