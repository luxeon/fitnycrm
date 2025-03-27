package com.fitnycrm.user.service.trainer;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.UserRoleRepository;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.repository.entity.UserRole.Name;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.service.exception.RoleNotFoundException;
import com.fitnycrm.user.service.trainer.mapper.TrainerRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TrainerRequestMapper requestMapper;
    private final TenantService tenantService;

    @Transactional
    public User create(UUID tenantId, CreateTrainerRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        User trainer = requestMapper.toUser(request);
        UserRole trainerRole = userRoleRepository.findByName(UserRole.Name.TRAINER)
                .orElseThrow(() -> new RoleNotFoundException(Name.TRAINER));
        
        trainer.setRoles(Set.of(trainerRole));
        trainer.setTenants(Set.of(tenant));
        tenant.getUsers().add(trainer);
        
        return userRepository.save(trainer);
    }
} 