package com.fittrackcrm.core.auth.service;

import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.auth.service.exception.InvalidCredentialsException;
import com.fittrackcrm.core.common.security.JwtUtils;
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
            User user = (User) authentication.getPrincipal();
            return jwtUtils.generateToken(user);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
} 