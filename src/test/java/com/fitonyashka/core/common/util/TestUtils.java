package com.fitonyashka.core.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.fitonyashka.core.admin.repository.entity.Admin;
import com.fitonyashka.core.common.config.JwtProperties;

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

    public static String generateTestJwtToken(Admin admin, JwtProperties jwtProperties) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("firstName", admin.getFirstName());
        claims.put("lastName", admin.getLastName());
        claims.put("email", admin.getEmail());
        claims.put("phoneNumber", admin.getPhoneNumber());
        claims.put("role", "ROLE_ADMIN");

        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)
                .subject(admin.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(key)
                .compact();
    }
} 