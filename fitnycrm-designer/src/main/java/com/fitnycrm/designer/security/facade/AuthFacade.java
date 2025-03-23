package com.fitnycrm.designer.security.facade;

import com.fitnycrm.designer.security.facade.mapper.AuthMapper;
import com.fitnycrm.designer.security.rest.model.AdminDetailsResponse;
import com.fitnycrm.designer.security.rest.model.AdminSignupRequest;
import com.fitnycrm.designer.security.rest.model.AuthRequest;
import com.fitnycrm.designer.security.rest.model.AuthResponse;
import com.fitnycrm.designer.security.service.AuthService;
import com.fitnycrm.designer.user.repository.entity.User;
import com.fitnycrm.designer.user.service.UserService;
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
        user = userService.create(user);
        return mapper.toAdminDetailsResponse(user);
    }

    public void confirmEmail(String token) {
        userService.confirmEmail(token);
    }
}
