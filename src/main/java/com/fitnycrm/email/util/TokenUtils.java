package com.fitnycrm.email.util;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

public class TokenUtils {

    private static final int TOKEN_LENGTH = 32;
    private static final int TOKEN_EXPIRATION_HOURS = 24;

    public static String generateToken() {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public static OffsetDateTime calculateExpirationTime() {
        return OffsetDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS);
    }

    public static boolean isTokenExpired(OffsetDateTime expirationTime) {
        return expirationTime != null && OffsetDateTime.now().isAfter(expirationTime);
    }
} 