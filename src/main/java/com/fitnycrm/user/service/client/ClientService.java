package com.fitnycrm.user.service.client;

import com.fitnycrm.email.service.EmailService;
import com.fitnycrm.email.util.TokenUtils;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.repository.ClientInvitationRepository;
import com.fitnycrm.user.repository.UserRepository;
import com.fitnycrm.user.repository.UserRoleRepository;
import com.fitnycrm.user.repository.entity.ClientInvitation;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import com.fitnycrm.user.rest.client.model.CreateClientRequest;
import com.fitnycrm.user.rest.client.model.SignupClientRequest;
import com.fitnycrm.user.rest.client.model.UpdateClientRequest;
import com.fitnycrm.user.service.client.exception.TenantAlreadyContainsUserException;
import com.fitnycrm.user.service.client.mapper.ClientRequestMapper;
import com.fitnycrm.user.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository repository;
    private final UserRoleRepository roleRepository;
    private final TenantService tenantService;
    private final ClientRequestMapper requestMapper;
    private final EmailService emailService;
    private final ClientInvitationRepository invitationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
        return repository.findByIdAndTenantAndRole(tenantId, UserRole.Name.CLIENT, clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
    }

    @Transactional(readOnly = true)
    public Page<User> findByTenantId(UUID tenantId, Pageable pageable) {
        return repository.findByTenantIdAndRole(tenantId, UserRole.Name.CLIENT, pageable);
    }

    @Transactional
    public void invite(UUID tenantId, String email, UUID inviterId) {
        Tenant tenant = tenantService.findById(tenantId);
        repository.findByEmail(email).ifPresent(user -> {
            if (user.getTenants().contains(tenant)) {
                throw new TenantAlreadyContainsUserException(tenantId, user.getId());
            }
        });

        User inviter = repository.findById(inviterId).orElseThrow(() -> new UserNotFoundException(inviterId));

        Optional<ClientInvitation> optionalInvitation = invitationRepository.findByTenantAndEmail(tenant, email);
        ClientInvitation invitation = optionalInvitation.orElseGet(ClientInvitation::new);

        invitation.setTenant(tenant);
        invitation.setEmail(email);
        invitation.setInviter(inviter);
        invitation.setExpiresAt(TokenUtils.calculateExpirationTime());
        invitation.setCreatedAt(OffsetDateTime.now());
        invitationRepository.save(invitation);

        emailService.sendClientInvitation(tenant, invitation, inviter);
    }

    @Transactional
    public User signup(UUID tenantId, UUID clientInvitationId, SignupClientRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        ClientInvitation invitation = invitationRepository.findById(clientInvitationId)
                .orElseThrow(() -> new InvitationNotFoundException(clientInvitationId));
        if (!invitation.getTenant().equals(tenant)) {
            throw new InvitationNotFoundException(clientInvitationId);
        }
        if (TokenUtils.isTokenExpired(invitation.getExpiresAt())) {
            throw new InvitationExpiredException(clientInvitationId);
        }

        User user = new User();
        user.setEmail(invitation.getEmail());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setPassword(passwordEncoder.encode(request.password()));

        UserRole role = roleRepository.findByName(UserRole.Name.CLIENT).orElseThrow(() ->
                new RoleNotFoundException(UserRole.Name.CLIENT));

        user.getRoles().add(role);
        user.getTenants().add(tenant);
        user = repository.save(user);

        invitationRepository.delete(invitation);
        return user;
    }
}
