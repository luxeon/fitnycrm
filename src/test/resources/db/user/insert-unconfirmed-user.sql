INSERT INTO users(id, tenant_id, first_name, last_name, email, phone_number, password, email_confirmed, confirmation_token, confirmation_token_expires_at)
VALUES(
    '935ac7f5-3e4f-462a-a76d-524bd3a5fd01',
    '7a7632b1-e932-48fd-9296-001036b4ec19',
    'Jorge',
    'Jonson',
    'jorge.jonson@example.com',
    '+987654321',
    '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG',
    false,
    'valid-confirmation-token',
    CURRENT_TIMESTAMP + INTERVAL '1 day'
);

INSERT INTO users(id, tenant_id, first_name, last_name, email, phone_number, password, email_confirmed, confirmation_token, confirmation_token_expires_at)
VALUES(
    '935ac7f5-3e4f-462a-a76d-524bd3a5fd02',
    '7a7632b1-e932-48fd-9296-001036b4ec19',
    'Jane',
    'Smith',
    'jane.smith@example.com',
    '+987654322',
    '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG',
    false,
    'expired-token',
    CURRENT_TIMESTAMP - INTERVAL '1 day'
); 