package com.fitnycrm.user.service.auth;

import com.fitnycrm.user.service.admin.AdminUserService;
import com.fitnycrm.user.service.auth.mapper.AuthenticatedUserDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserDetailsService implements UserDetailsService {

    private final AdminUserService adminUserService;
    private final AuthenticatedUserDetailsMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adminUserService.findByEmail(email)
                .map(mapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    }
}
