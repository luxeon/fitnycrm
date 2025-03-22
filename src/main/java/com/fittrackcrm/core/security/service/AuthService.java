package com.fittrackcrm.core.security.service;

import com.fittrackcrm.core.security.service.exception.InvalidCredentialsException;
import com.fittrackcrm.core.security.service.model.UserDetailsImpl;
import com.fittrackcrm.core.security.util.JwtUtils;
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
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            return jwtUtils.generateToken(user);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
} 