package com.fittrackcrm.core.security.util;

import com.fittrackcrm.core.security.service.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenCreator {

    private static final String BEARER = "Bearer ";

    private static final UUID TENANT_ID = UUID.fromString("7a7632b1-e932-48fd-9296-001036b4ec19");

    private static final UUID ADMIN_USER_ID = UUID.fromString("835ac7f5-3e4f-462a-a76d-524bd3a5fd00");
    private static final String ADMIN_FIRST_NAME = "Max";
    private static final String ADMIN_LAST_NAME = "Power";
    private static final String ADMIN_EMAIL = "max.power@gmail.com";
    private static final String ADMIN_PHONE = "+123456789";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final JwtUtils jwtUtils;

    public String generateAdminTestJwtToken() {
        UserDetailsImpl user = new UserDetailsImpl();
        user.setId(ADMIN_USER_ID);
        user.setTenantId(TENANT_ID);
        user.setFirstName(ADMIN_FIRST_NAME);
        user.setLastName(ADMIN_LAST_NAME);
        user.setEmail(ADMIN_EMAIL);
        user.setPhoneNumber(ADMIN_PHONE);
        user.setAuthorities(List.of(new SimpleGrantedAuthority(ADMIN_ROLE)));
        return BEARER + jwtUtils.generateToken(user);
    }
}
