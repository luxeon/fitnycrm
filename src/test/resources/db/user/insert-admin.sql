INSERT INTO users(id, tenant_id, first_name, last_name, email, phone_number, password)
VALUES('835ac7f5-3e4f-462a-a76d-524bd3a5fd00', '7a7632b1-e932-48fd-9296-001036b4ec19', 'Max', 'Power', 'max.power@gmail.com', '+123456789', '$2a$10$6ndbi.td4IJOnRhr3VvfwOG2dr4X4l.9SAZRsFo5y2ukcz697LXOG');

INSERT INTO user_roles(user_id, role_id)
VALUES ('835ac7f5-3e4f-462a-a76d-524bd3a5fd00', '92395bf1-a253-4b79-b0eb-969863d62a6f');