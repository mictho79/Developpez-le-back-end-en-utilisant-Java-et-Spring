package com.chatop.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String secret = "123456789";

    public String extractEmail(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();
    }

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .sign(Algorithm.HMAC512(secret));
    }
}
