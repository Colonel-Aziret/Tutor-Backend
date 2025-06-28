package com.example.tutor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Генерация ключа HMAC SHA-256
    private static final String SECRET = "YmFzZTY0RW5jb2RlZFNlY3JldEtleU5vdEhhcmQhMTIz";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());


    /**
     * Создание JWT токена
     *
     * @param username - имя пользователя
     * @return сгенерированный токен
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claim("claims", claims) // Вместо setClaims
                .subject(subject)    // setSubject работает, но можно заменить на claim
                .issuedAt(new Date(System.currentTimeMillis())) // Вместо setIssuedAt
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // expiresAt вместо setExpiration
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Проверка валидности токена
     *
     * @param token    - токен
     * @param username - имя пользователя
     * @return true, если токен валиден
     */
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Извлечение имени пользователя из токена
     *
     * @param token - токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Извлечение всех утверждений (claims) из токена
     *
     * @param token - токен
     * @return объект Claims
     */
    public Claims extractAllClaims(String token) {
        // Использование Jwts.parserBuilder() для работы с токенами
        return Jwts.parser()  // Создание JwtParserBuilder
                .setSigningKey(SECRET_KEY) // Установка секретного ключа
                .build().parseSignedClaims(token).getPayload(); // Извлечение тела токена (Claims)
    }

    /**
     * Проверка, истёк ли срок действия токена
     *
     * @param token - токен
     * @return true, если срок действия истёк
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
