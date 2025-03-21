package com.fitonyashka.core.auth.service;

import com.fitonyashka.core.admin.repository.entity.Admin;
import com.fitonyashka.core.auth.service.exception.InvalidCredentialsException;
import com.fitonyashka.core.common.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional(readOnly = true)
    public String authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            Admin admin = (Admin) authentication.getPrincipal();
            return jwtUtils.generateToken(admin);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
} 