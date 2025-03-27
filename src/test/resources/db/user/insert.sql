INSERT INTO users(id, first_name, last_name, email, phone_number, password)
VALUES ('835ac7f5-3e4f-462a-a76d-524bd3a5fd00', 'Max', 'Power', 'max.power@gmail.com', '+123456789',
        '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG');

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', '835ac7f5-3e4f-462a-a76d-524bd3a5fd00');

INSERT INTO user_roles(user_id, role_id)
VALUES ('835ac7f5-3e4f-462a-a76d-524bd3a5fd00', '92395bf1-a253-4b79-b0eb-969863d62a6f');

-- User without tenant
INSERT INTO users(id, first_name, last_name, email, phone_number, password)
VALUES ('a35ac7f5-3e4f-462a-a76d-524bd3a5fd02', 'Jane', 'Smith', 'jane.smith@gmail.com', '+111222333',
        '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG');

INSERT INTO user_roles(user_id, role_id)
VALUES ('a35ac7f5-3e4f-462a-a76d-524bd3a5fd02', '92395bf1-a253-4b79-b0eb-969863d62a6f');

-- Test users for role-based access control tests
INSERT INTO users(id, first_name, last_name, email, phone_number, password, created_at, updated_at)
VALUES ('b35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'Test', 'User', 'test.user@gmail.com', '+444555666',
        '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG', now(), now());

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', 'b35ac7f5-3e4f-462a-a76d-524bd3a5fd03');

-- Client role
INSERT INTO user_roles(user_id, role_id)
VALUES ('b35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'e90a9385-08cb-4489-8191-7c0e1de82b7d');

-- Trainer role
INSERT INTO user_roles(user_id, role_id)
VALUES ('b35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'ef32d172-df6f-4a19-8eff-ae70ac6852d7');