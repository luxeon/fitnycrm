package com.fittrackcrm.core.user.facade;

import org.springframework.stereotype.Component;

import com.fittrackcrm.core.user.facade.mapper.UserMapper;
import com.fittrackcrm.core.user.rest.model.UserDetailsResponse;
import com.fittrackcrm.core.user.rest.model.UserSignupRequest;
import com.fittrackcrm.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserDetailsResponse signup(UUID tenantId, UserSignupRequest request) {
        return userMapper.toResponse(
            userService.createClient(
                userMapper.toEntity(tenantId, request)
            )
        );
    }
} 