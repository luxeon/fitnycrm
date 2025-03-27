package com.fitnycrm.user.service.trainer;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.UserRoleRepository;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.repository.entity.UserRole.Name;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.rest.trainer.model.UpdateTrainerRequest;
import com.fitnycrm.user.service.exception.RoleNotFoundException;
import com.fitnycrm.user.service.exception.UserEmailAlreadyExistsException;
import com.fitnycrm.user.service.exception.UserNotFoundException;
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
    private final UserRoleRepository roleRepository;
    private final TrainerRequestMapper requestMapper;
    private final TenantService tenantService;

    @Transactional
    public User create(UUID tenantId, CreateTrainerRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserEmailAlreadyExistsException(request.email());
        }
        Tenant tenant = tenantService.findById(tenantId);
        User trainer = requestMapper.toUser(request);
        UserRole trainerRole = roleRepository.findByName(UserRole.Name.TRAINER)
                .orElseThrow(() -> new RoleNotFoundException(Name.TRAINER));

        trainer.setRoles(Set.of(trainerRole));
        trainer.setTenants(Set.of(tenant));
        tenant.getUsers().add(trainer);

        return userRepository.save(trainer);
    }

    @Transactional
    public User update(UUID tenantId, UUID trainerId, UpdateTrainerRequest request) {
        User trainer = findById(tenantId, trainerId);

        if (!trainer.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new UserEmailAlreadyExistsException(request.email());
        }

        requestMapper.updateUser(trainer, request);
        return userRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public User findById(UUID tenantId, UUID trainerId) {
        return userRepository.findByIdAndTenant(tenantId, trainerId)
                .orElseThrow(() -> new UserNotFoundException(trainerId));
    }

    @Transactional
    public void delete(UUID tenantId, UUID trainerId) {
        User client = findById(tenantId, trainerId);
        Set<UserRole> roles = client.getRoles();
        roles.forEach(role -> role.getUsers().remove(client));

        Set<Tenant> tenants = client.getTenants();
        tenants.forEach(tenant -> tenant.getUsers().remove(client));

        userRepository.delete(client);
    }
}