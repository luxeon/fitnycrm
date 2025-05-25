package com.fitavera.user.facade.auth;

import com.fitavera.user.facade.auth.mapper.AdminResponseMapper;
import com.fitavera.user.facade.auth.mapper.AuthResponseMapper;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.model.AdminDetailsResponse;
import com.fitavera.user.rest.model.AuthRequest;
import com.fitavera.user.rest.model.AuthResponse;
import com.fitavera.user.rest.model.CreateAdminRequest;
import com.fitavera.user.service.admin.AdminUserService;
import com.fitavera.user.service.auth.AuthService;
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
