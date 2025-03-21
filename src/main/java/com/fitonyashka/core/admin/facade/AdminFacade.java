package com.fitonyashka.core.admin.facade;

import org.springframework.stereotype.Component;

import com.fitonyashka.core.admin.facade.mapper.AdminMapper;
import com.fitonyashka.core.admin.rest.model.AdminDetailsResponse;
import com.fitonyashka.core.admin.rest.model.AdminSignupRequest;
import com.fitonyashka.core.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFacade {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    public AdminDetailsResponse signup(AdminSignupRequest request) {
        return adminMapper.toResponse(
            adminService.signup(
                adminMapper.toEntity(request)
            )
        );
    }
} 