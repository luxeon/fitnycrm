--

INSERT INTO users (id, first_name, last_name, email, phone_number, created_at, updated_at)
VALUES ('07779e45-c1b6-407d-a82c-c72324abe580',
        'Bill',
        'Gates',
        'bill.gates@example.com',
        '+4567890123',
        '2024-03-20T10:00:00Z',
        '2024-03-20T10:00:00Z');

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('89a5a049-6aa5-40a3-9202-3cb2a7690972', '07779e45-c1b6-407d-a82c-c72324abe580');

INSERT INTO user_roles(user_id, role_id)
VALUES ('07779e45-c1b6-407d-a82c-c72324abe580', 'e90a9385-08cb-4489-8191-7c0e1de82b7d');