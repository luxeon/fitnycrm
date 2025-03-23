package com.fitnycrm.designer.security.util;

import com.fitnycrm.designer.security.service.model.AuthenticatedUserDetails;
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

    private static final UUID USER_WITHOUT_TENANT_ID = UUID.fromString("a35ac7f5-3e4f-462a-a76d-524bd3a5fd02");
    private static final String USER_WITHOUT_TENANT_FIRST_NAME = "Jane";
    private static final String USER_WITHOUT_TENANT_LAST_NAME = "Smith";
    private static final String USER_WITHOUT_TENANT_EMAIL = "jane.smith@gmail.com";
    private static final String USER_WITHOUT_TENANT_PHONE = "+111222333";

    private final JwtUtils jwtUtils;

    public String generateAdminTestJwtToken() {
        AuthenticatedUserDetails user = new AuthenticatedUserDetails();
        user.setId(ADMIN_USER_ID);
        user.setTenantId(TENANT_ID);
        user.setFirstName(ADMIN_FIRST_NAME);
        user.setLastName(ADMIN_LAST_NAME);
        user.setEmail(ADMIN_EMAIL);
        user.setPhoneNumber(ADMIN_PHONE);
        user.setAuthorities(List.of(new SimpleGrantedAuthority(ADMIN_ROLE)));
        return BEARER + jwtUtils.generateToken(user);
    }

    public String generateAdminWithoutTenantTestJwtToken() {
        AuthenticatedUserDetails user = new AuthenticatedUserDetails();
        user.setId(USER_WITHOUT_TENANT_ID);
        user.setTenantId(null);
        user.setFirstName(USER_WITHOUT_TENANT_FIRST_NAME);
        user.setLastName(USER_WITHOUT_TENANT_LAST_NAME);
        user.setEmail(USER_WITHOUT_TENANT_EMAIL);
        user.setPhoneNumber(USER_WITHOUT_TENANT_PHONE);
        user.setAuthorities(List.of(new SimpleGrantedAuthority(ADMIN_ROLE)));
        return BEARER + jwtUtils.generateToken(user);
    }
}
