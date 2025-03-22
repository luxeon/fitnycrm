package com.fittrackcrm.core.user.facade;

import com.fittrackcrm.core.user.facade.mapper.UserMapper;
import com.fittrackcrm.core.user.rest.model.UserDetailsResponse;
import com.fittrackcrm.core.user.rest.model.UserSignupRequest;
import com.fittrackcrm.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserDetailsResponse createClient(UUID tenantId, UserSignupRequest request) {
        return userMapper.toResponse(
            userService.createClient(
                userMapper.toEntity(tenantId, request)
            )
        );
    }
} 