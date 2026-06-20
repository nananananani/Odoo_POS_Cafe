package com.cafepos.security;

import com.cafepos.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_STRING = "your-very-secure-and-long-secret-key-for-jwt-cafepos-app-must-be-256-bits";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private final long EXPIRATION_MS = 86400000; // 24 hours

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Long getIdFromToken(String token) {
        Object id = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("id");
        if (id instanceof Number) {
            return ((Number) id).longValue();
        }
        return null;
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
}
