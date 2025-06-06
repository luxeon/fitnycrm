package com.fitavera.user.service.client;

import com.fitavera.email.service.EmailService;
import com.fitavera.email.util.TokenUtils;
import com.fitavera.tenant.repository.entity.Tenant;
import com.fitavera.tenant.service.TenantService;
import com.fitavera.user.repository.ClientInvitationRepository;
import com.fitavera.user.repository.UserRepository;
import com.fitavera.user.repository.UserRoleRepository;
import com.fitavera.user.repository.entity.ClientInvitation;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.repository.entity.UserRole;
import com.fitavera.user.rest.client.model.SignupClientRequest;
import com.fitavera.user.rest.client.model.UpdateClientRequest;
import com.fitavera.user.service.client.exception.TenantAlreadyContainsUserException;
import com.fitavera.user.service.client.mapper.ClientRequestMapper;
import com.fitavera.user.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
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
    public User update(UUID tenantId, UUID clientId, UpdateClientRequest request) {
        User user = findById(tenantId, clientId);
        if (!user.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new UserEmailAlreadyExistsException(request.email());
        }
        requestMapper.update(user, request);
        return repository.save(user);
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
        user.setLocale(Locale.forLanguageTag(request.locale()));
        user.setPassword(passwordEncoder.encode(request.password()));

        UserRole role = roleRepository.findByName(UserRole.Name.CLIENT).orElseThrow(() ->
                new RoleNotFoundException(UserRole.Name.CLIENT));

        user.getRoles().add(role);
        user.getTenants().add(tenant);
        tenant.getUsers().add(user);
        user = repository.save(user);

        invitationRepository.delete(invitation);
        return user;
    }

    @Transactional
    public void joinByInvitation(UUID tenantId, UUID clientInvitationId, UUID userId) {
        Tenant tenant = tenantService.findById(tenantId);
        ClientInvitation invitation = invitationRepository.findById(clientInvitationId)
                .orElseThrow(() -> new InvitationNotFoundException(clientInvitationId));

        if (!invitation.getTenant().equals(tenant)) {
            throw new InvitationNotFoundException(clientInvitationId);
        }
        if (TokenUtils.isTokenExpired(invitation.getExpiresAt())) {
            throw new InvitationExpiredException(clientInvitationId);
        }

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.getEmail().equals(invitation.getEmail())) {
            throw new InvitationNotFoundException(clientInvitationId);
        }

        if (user.getTenants().contains(tenant)) {
            throw new TenantAlreadyContainsUserException(tenantId, userId);
        }

        user.getTenants().add(tenant);
        tenant.getUsers().add(user);
        repository.save(user);

        invitationRepository.delete(invitation);
    }
}
