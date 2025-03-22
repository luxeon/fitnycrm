package com.fittrackcrm.core.client.service;

import com.fittrackcrm.core.client.repository.ClientRepository;
import com.fittrackcrm.core.client.repository.entity.Client;
import com.fittrackcrm.core.client.service.exception.ClientEmailAlreadyExistsException;
import com.fittrackcrm.core.user.service.exception.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    @Transactional
    public Client createClient(Client client) {
        if (repository.existsByTenantIdAndEmail(client.getTenantId(), client.getEmail())) {
            throw new ClientEmailAlreadyExistsException(client.getEmail());
        }
        return repository.save(client);
    }
}
