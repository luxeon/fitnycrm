package com.fittrackcrm.core.user.service;

import com.fittrackcrm.core.email.service.EmailService;
import com.fittrackcrm.core.email.util.TokenUtils;
import com.fittrackcrm.core.security.service.exception.InvalidEmailConfirmationTokenException;
import com.fittrackcrm.core.user.repository.UserRepository;
import com.fittrackcrm.core.user.repository.UserRoleRepository;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.repository.entity.UserRole;
import com.fittrackcrm.core.user.service.exception.RoleNotFoundException;
import com.fittrackcrm.core.user.service.exception.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }

        UserRole role = roleRepository.findByName(UserRole.Name.ADMIN).orElseThrow(() ->
                new RoleNotFoundException(UserRole.Name.ADMIN));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        user.setEmailConfirmed(false);
        user.setConfirmationToken(TokenUtils.generateToken());
        user.setConfirmationTokenExpiresAt(TokenUtils.calculateExpirationTime());

        user = userRepository.save(user);

        emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken());

        return user;
    }

    @Transactional
    public void confirmEmail(String token) {
        User user = userRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new InvalidEmailConfirmationTokenException("Invalid confirmation token"));

        if (TokenUtils.isTokenExpired(user.getConfirmationTokenExpiresAt())) {
            throw new InvalidEmailConfirmationTokenException("Confirmation token has expired");
        }

        user.setEmailConfirmed(true);
        user.setConfirmationToken(null);
        user.setConfirmationTokenExpiresAt(null);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
} 