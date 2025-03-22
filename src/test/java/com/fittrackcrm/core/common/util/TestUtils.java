package com.fittrackcrm.core.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import com.fittrackcrm.core.security.service.model.UserDetailsImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.security.config.JwtProperties;

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
} 