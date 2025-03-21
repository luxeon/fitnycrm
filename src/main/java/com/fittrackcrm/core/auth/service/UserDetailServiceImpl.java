package com.fittrackcrm.core.auth.service;

import com.fittrackcrm.core.auth.service.mapper.UserDetailsMapper;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserDetailsMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.findByEmail(email)
                .map(mapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    }
}
