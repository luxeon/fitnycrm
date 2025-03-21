package com.fittrackcrm.core.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.auth.config.JwtProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TestUtils {

    public static String readFile(String path) {
        try {
            var resource = new ClassPathResource(path);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public static String generateTestJwtToken(User user, JwtProperties jwtProperties) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("email", user.getEmail());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", "ROLE_ADMIN");

        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(key)
                .compact();
    }
} 