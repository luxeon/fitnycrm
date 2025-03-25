package com.fitnycrm.user.facade;

import com.fitnycrm.user.facade.mapper.AuthMapper;
import com.fitnycrm.user.rest.model.UserDetailsResponse;
import com.fitnycrm.user.rest.model.UserSignupRequest;
import com.fitnycrm.user.rest.model.AuthRequest;
import com.fitnycrm.user.rest.model.AuthResponse;
import com.fitnycrm.user.service.AuthService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.UserService;
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

    public UserDetailsResponse signup(UserSignupRequest request) {
        User user = mapper.toUser(request);
        user = userService.create(user);
        return mapper.toAdminDetailsResponse(user);
    }

    public void confirmEmail(String token) {
        userService.confirmEmail(token);
    }
}
