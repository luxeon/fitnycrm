package com.fitnycrm.client.service;

import com.fitnycrm.client.repository.ClientRepository;
import com.fitnycrm.client.repository.entity.Client;
import com.fitnycrm.client.service.exception.ClientEmailAlreadyExistsException;
import com.fitnycrm.client.service.exception.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    @Transactional
    public Client create(Client client) {
        if (repository.existsByTenantIdAndEmail(client.getTenantId(), client.getEmail())) {
            throw new ClientEmailAlreadyExistsException(client.getEmail());
        }
        return repository.save(client);
    }

    @Transactional
    public Client update(UUID tenantId, UUID clientId, Client client) {
        Client existingClient = repository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!existingClient.getTenantId().equals(tenantId)) {
            throw new ClientNotFoundException(clientId);
        }

        if (!existingClient.getEmail().equals(client.getEmail()) &&
                repository.existsByTenantIdAndEmail(tenantId, client.getEmail())) {
            throw new ClientEmailAlreadyExistsException(client.getEmail());
        }

        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setEmail(client.getEmail());
        existingClient.setPhoneNumber(client.getPhoneNumber());

        return repository.save(existingClient);
    }

    @Transactional
    public void delete(UUID tenantId, UUID clientId) {
        Client client = repository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!client.getTenantId().equals(tenantId)) {
            throw new ClientNotFoundException(clientId);
        }

        repository.delete(client);
    }

    @Transactional(readOnly = true)
    public Client findById(UUID tenantId, UUID clientId) {
        Client client = repository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        if (!client.getTenantId().equals(tenantId)) {
            throw new ClientNotFoundException(clientId);
        }

        return client;
    }
}
