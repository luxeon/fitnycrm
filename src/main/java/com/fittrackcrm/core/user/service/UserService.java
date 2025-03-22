package com.fittrackcrm.core.user.service;

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

    @Transactional
    public User createClient(User user) {
        return createUser(user, UserRole.Name.CLIENT);
    }

    @Transactional
    public User createAdmin(User user) {
        return createUser(user, UserRole.Name.ADMIN);
    }

    private User createUser(User user, UserRole.Name roleName) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }
        UserRole role = roleRepository.findByName(roleName).orElseThrow(() ->
                new RoleNotFoundException(roleName));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
} 