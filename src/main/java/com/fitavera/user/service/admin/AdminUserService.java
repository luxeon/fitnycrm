package com.fitavera.user.service.admin;

import com.fitavera.email.service.EmailService;
import com.fitavera.email.util.TokenUtils;
import com.fitavera.user.repository.UserRepository;
import com.fitavera.user.repository.UserRoleRepository;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.repository.entity.UserRole;
import com.fitavera.user.rest.model.CreateAdminRequest;
import com.fitavera.user.service.admin.mapper.UserRequestMapper;
import com.fitavera.user.service.exception.InvalidEmailConfirmationTokenException;
import com.fitavera.user.service.exception.RoleNotFoundException;
import com.fitavera.user.service.exception.UserEmailAlreadyExistsException;
import com.fitavera.user.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRequestMapper requestMapper;

    @Transactional
    public User createAdmin(CreateAdminRequest request) {
        User user = requestMapper.toUser(request);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }

        UserRole role = roleRepository.findByName(UserRole.Name.ADMIN).orElseThrow(() ->
                new RoleNotFoundException(UserRole.Name.ADMIN));
        user.setRoles(Set.of(role));

        user.setLocale(Locale.forLanguageTag(request.locale()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailConfirmed(false);
        user.setConfirmationToken(TokenUtils.generateToken());
        user.setConfirmationTokenExpiresAt(TokenUtils.calculateExpirationTime());

        user = userRepository.save(user);

        emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken(), user.getLocale());

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

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}