package com.fittrackcrm.core.security.facade;

import com.fittrackcrm.core.security.facade.mapper.AuthMapper;
import com.fittrackcrm.core.security.rest.model.AdminDetailsResponse;
import com.fittrackcrm.core.security.rest.model.AdminSignupRequest;
import com.fittrackcrm.core.security.rest.model.AuthRequest;
import com.fittrackcrm.core.security.rest.model.AuthResponse;
import com.fittrackcrm.core.security.service.AuthService;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final UserService userService;
    private final AuthMapper mapper;

    public AuthResponse authenticate(AuthRequest request) {
        return mapper.toAuthResponse(authService.authenticate(request.email(), request.password()));
    }

    public AdminDetailsResponse signup(AdminSignupRequest request) {
        User user = mapper.toUser(request);
        user = userService.createAdmin(user);
        return mapper.toAdminDetailsResponse(user);
    }
}
