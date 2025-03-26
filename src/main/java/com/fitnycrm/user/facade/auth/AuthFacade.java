package com.fitnycrm.user.facade.auth;

import com.fitnycrm.user.facade.auth.mapper.AdminResponseMapper;
import com.fitnycrm.user.facade.auth.mapper.AuthResponseMapper;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.model.AdminDetailsResponse;
import com.fitnycrm.user.rest.model.CreateAdminRequest;
import com.fitnycrm.user.rest.model.AuthRequest;
import com.fitnycrm.user.rest.model.AuthResponse;
import com.fitnycrm.user.service.auth.AuthService;
import com.fitnycrm.user.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final AdminUserService adminUserService;
    private final AuthResponseMapper authResponseMapper;
    private final AdminResponseMapper adminResponseMapper;

    public AuthResponse authenticate(AuthRequest request) {
        return authResponseMapper.toAuthResponse(authService.authenticate(request.email(), request.password()));
    }

    public AdminDetailsResponse signup(CreateAdminRequest request) {
        User user = adminUserService.createAdmin(request);
        return adminResponseMapper.toDetailsResponse(user);
    }

    public void confirmEmail(String token) {
        adminUserService.confirmEmail(token);
    }
}
