package com.example.tutor.security;


import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.ErrorMessage;
import com.example.tutor.db.repository.ErrorMessageRepository;
import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.errorMessage.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final ErrorMessageRepository errorMessageRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${server.servlet.context-path}")
    private String appContextPath;

    public JwtRequestFilter(ErrorMessageRepository errorMessageRepository, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.errorMessageRepository = errorMessageRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            authenticateUser(jwt, request, response);
        }
        chain.doFilter(request, response);
    }

    private void authenticateUser(String jwt, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String username = jwtUtil.extractUsername(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                if (!response.isCommitted()) {
                    sendJsonErrorResponse(response, "error.token.expired_or_invalid", HttpStatus.FORBIDDEN);
                }
            }
        } catch (Exception e) {
            if (!response.isCommitted()) {
                sendJsonErrorResponse(response, "error.token.expired_or_invalid", HttpStatus.FORBIDDEN);
            }
        }
    }

    public void sendJsonErrorResponse(HttpServletResponse response, Object message, HttpStatus status) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            Optional<ErrorMessage> byId = errorMessageRepository.findById(message.toString());
            Object errorMessage;
            if (byId.isPresent()) {
                errorMessage = List.of(ErrorDto.from(byId.get()));
            } else {
                errorMessage = message.toString();
            }

            BaseResponse errorResponse = BaseResponse.builder()
                    .success(BaseController.Constants.ERROR)
                    .msg(errorMessage)
                    .build();
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
        }
    }
}
