INSERT INTO client_invitations (id, tenant_id, email, inviter_id, expires_at, created_at)
VALUES ('d45ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        '7a7632b1-e932-48fd-9296-001036b4ec19',
        'new.client@example.com',
        '835ac7f5-3e4f-462a-a76d-524bd3a5fd00',
        now()::date + interval '1 day',
        now()::date - interval '1 day');

INSERT INTO client_invitations (id, tenant_id, email, inviter_id, expires_at, created_at)
VALUES ('54b2531a-7eda-49d9-abc4-c33164bb9ffe',
        '7a7632b1-e932-48fd-9296-001036b4ec19',
        'new.client2@example.com',
        '835ac7f5-3e4f-462a-a76d-524bd3a5fd00',
        now()::date - interval '1 day',
        now()::date - interval '2 day');

INSERT INTO client_invitations (id, tenant_id, email, inviter_id, expires_at, created_at)
VALUES ('ac478971-4bb7-493d-93ca-8e9bbe381554',
        '7a7632b1-e932-48fd-9296-001036b4ec19',
        'bill.gates@example.com',
        '835ac7f5-3e4f-462a-a76d-524bd3a5fd00',
        now()::date + interval '1 day',
        now()::date - interval '1 day');

INSERT INTO client_invitations (id, tenant_id, email, inviter_id, expires_at, created_at)
VALUES ('548ca827-f805-4638-b5b7-da7b6545d26c',
        '7a7632b1-e932-48fd-9296-001036b4ec19',
        'james.bond@example.com',
        '835ac7f5-3e4f-462a-a76d-524bd3a5fd00',
        now()::date - interval '1 day',
        now()::date - interval '2 day');