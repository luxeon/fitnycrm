package com.fitavera.user.service.trainer;

import com.fitavera.tenant.repository.entity.Tenant;
import com.fitavera.tenant.service.TenantService;
import com.fitavera.user.repository.UserRepository;
import com.fitavera.user.repository.UserRoleRepository;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.repository.entity.UserRole;
import com.fitavera.user.repository.entity.UserRole.Name;
import com.fitavera.user.rest.trainer.model.CreateTrainerRequest;
import com.fitavera.user.rest.trainer.model.UpdateTrainerRequest;
import com.fitavera.user.service.exception.RoleNotFoundException;
import com.fitavera.user.service.exception.UserEmailAlreadyExistsException;
import com.fitavera.user.service.exception.UserNotFoundException;
import com.fitavera.user.service.trainer.mapper.TrainerRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        User trainer = requestMapper.toUser(request, tenant.getLocale());
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
        return userRepository.findByIdAndTenantAndRole(tenantId, Name.TRAINER, trainerId)
                .orElseThrow(() -> new UserNotFoundException(trainerId));
    }

    @Transactional(readOnly = true)
    public Page<User> findByTenantId(UUID tenantId, Pageable pageable) {
        return userRepository.findByTenantIdAndRole(tenantId, Name.TRAINER, pageable);
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