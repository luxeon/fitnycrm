INSERT INTO users (id, first_name, last_name, email, phone_number, created_at, updated_at)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'James',
        'Bond',
        'james.bond@example.com',
        '+12345678901',
        '2024-03-20T10:00:00Z',
        '2024-03-20T10:00:00Z');

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01');

INSERT INTO user_roles(user_id, role_id)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd01', 'e90a9385-08cb-4489-8191-7c0e1de82b7d');

INSERT INTO users (id, first_name, last_name, email, phone_number, created_at, updated_at)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd02',
        'Donald',
        'McDonald',
        'donald.mcdonald@example.com',
        '+12345678902',
        '2024-03-20T10:00:00Z',
        '2024-03-20T10:00:00Z');

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd02');

INSERT INTO user_roles(user_id, role_id)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd02', 'e90a9385-08cb-4489-8191-7c0e1de82b7d');