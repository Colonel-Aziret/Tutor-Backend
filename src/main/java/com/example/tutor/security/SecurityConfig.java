package com.example.tutor.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/user/admin/**").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/plan-register/delete-all").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/advertisment/create").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/advertisment/{id}").permitAll()
                        .requestMatchers("/user/{id}").permitAll()
                        .requestMatchers("/user/comment-ban/{userId}").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/user/comment-unban/{userId}").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/user/delete-comment/{id}").hasAnyAuthority("SUPER_ADMIN", "STUDENT")
                        .requestMatchers("/user/upload-photo").hasAnyAuthority("SUPER_ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers("/user/comment").hasAnyAuthority("SUPER_ADMIN", "STUDENT")
                        .requestMatchers(Constants.PUBLIC_ENDPOINTS).permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                        jwtRequestFilter.sendJsonErrorResponse(response, "error.access.denied", HttpStatus.FORBIDDEN);
                    });
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        jwtRequestFilter.sendJsonErrorResponse(response, "error.access.denied", HttpStatus.FORBIDDEN);
                    });
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*");
        config.addAllowedOriginPattern("http://localhost:3000");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token"));
        config.setExposedHeaders(List.of("X-Auth-Token"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    public static class Constants {
        public static final String[] PUBLIC_ENDPOINTS = {
                "/status",
                "/admin-panel/login",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-ui/**",
                "/swagger-ui/",
                "/webjars/**",
                "/auth/activate",
                "/auth/registration",
                "/user/get-photo",
                "/role/get-all",
                "/auth/forgot-password",
                "/auth/reset-password",
                "/auth/reset-password/{token}",
                "/files",
                "/files/**",
                "/hb-cities/get-all",
                "/hb-subjects/get-all",
                "/reviews/comments",
                "/user/get-all",
                "/user/{id}",
                "/advertisment/get-all",
                "/advertisment/{id}",

        };
    }

    @Bean
    public JavaMailSender getJavaMailSender(@Value("${spring.mail.host}") String host,
                                            @Value("${spring.mail.username}") String userName,
                                            @Value("${spring.mail.password}") String password,
                                            @Value("${spring.mail.port}") int port) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // 🔧 Ключевая строка
        props.put("mail.debug", "true");

        return mailSender;
    }
}
