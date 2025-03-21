package com.fittrackcrm.core.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fittrackcrm.core.user.repository.UserRepository;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.service.exception.UserEmailAlreadyExistsException;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
} 