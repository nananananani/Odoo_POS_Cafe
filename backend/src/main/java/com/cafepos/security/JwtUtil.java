package com.cafepos.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public String generateToken(String username) {
        return "";
    }

    public boolean validateToken(String token) {
        return true;
    }

    public String getUsernameFromToken(String token) {
        return "";
    }
}
