package com.fitnycrm.user.service.client;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.UserRoleRepository;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.rest.client.model.CreateClientRequest;
import com.fitnycrm.user.rest.client.model.UpdateClientRequest;
import com.fitnycrm.user.service.client.mapper.ClientRequestMapper;
import com.fitnycrm.user.service.exception.RoleNotFoundException;
import com.fitnycrm.user.service.exception.UserEmailAlreadyExistsException;
import com.fitnycrm.user.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository repository;
    private final UserRoleRepository roleRepository;
    private final TenantService tenantService;
    private final ClientRequestMapper requestMapper;

    @Transactional
    public User create(UUID tenantId, CreateClientRequest request) {
        User user = requestMapper.toUser(tenantId, request);
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }

        Tenant tenant = tenantService.findById(tenantId);
        UserRole role = roleRepository.findByName(UserRole.Name.CLIENT).orElseThrow(() ->
                new RoleNotFoundException(UserRole.Name.CLIENT));

        user.getRoles().add(role);
        user.getTenants().add(tenant);
        user = repository.save(user);
        return user;
    }

    @Transactional
    public User update(UUID tenantId, UUID clientId, UpdateClientRequest request) {
        User user = findById(tenantId, clientId);
        if (!user.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new UserEmailAlreadyExistsException(request.email());
        }
        requestMapper.update(user, request);
        return repository.save(user);
    }

    @Transactional
    public void delete(UUID tenantId, UUID clientId) {
        User client = findById(tenantId, clientId);
        Set<UserRole> roles = client.getRoles();
        roles.forEach(role -> role.getUsers().remove(client));

        Set<Tenant> tenants = client.getTenants();
        tenants.forEach(tenant -> tenant.getUsers().remove(client));

        repository.delete(client);
    }

    @Transactional(readOnly = true)
    public User findById(UUID tenantId, UUID clientId) {
        return repository.findByIdAndTenant(tenantId, clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
    }

    @Transactional(readOnly = true)
    public Page<User> findByTenantId(UUID tenantId, Pageable pageable) {
        return repository.findByTenantId(tenantId, pageable);
    }
}
