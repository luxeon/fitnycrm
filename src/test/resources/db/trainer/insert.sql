INSERT INTO users (id, email, first_name, last_name, phone_number, created_at, updated_at)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'john.trainer@example.com',
        'John', 'Trainer', '+1234567890', NOW(), NOW());

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd03');

INSERT INTO user_roles(user_id, role_id)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'ef32d172-df6f-4a19-8eff-ae70ac6852d7');