package com.fitnycrm.designer.client.service;

import com.fitnycrm.designer.client.repository.ClientRepository;
import com.fitnycrm.designer.client.repository.entity.Client;
import com.fitnycrm.designer.client.service.exception.ClientEmailAlreadyExistsException;
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
