-- Define 10 random users
-- User 1
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', 'User1', 'Test', 'user1@test.com', 'password', '+1234567890', now(),
        now(), TRUE);

INSERT INTO user_roles(user_id, role_id)
VALUES ('a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', 'e90a9385-08cb-4489-8191-7c0e1de82b7d');

INSERT INTO tenant_users(tenant_id, user_id)
VALUES ('7a7632b1-e932-48fd-9296-001036b4ec19', 'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01');

-- User 2
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('c45ac7f5-4e5f-462a-b76d-624bd3a5fd04', 'User2', 'Test', 'user2@test.com', 'password', '+1234567891', now(),
        now(), TRUE);

-- User 3
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('d55ac7f5-5e6f-462a-c76d-724bd3a5fd05', 'User3', 'Test', 'user3@test.com', 'password', '+1234567892', now(),
        now(), TRUE);

-- User 4
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('e65ac7f5-6e7f-462a-d76d-824bd3a5fd06', 'User4', 'Test', 'user4@test.com', 'password', '+1234567893', now(),
        now(), TRUE);

-- User 5
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('f75ac7f5-7e8f-462a-e76d-924bd3a5fd07', 'User5', 'Test', 'user5@test.com', 'password', '+1234567894', now(),
        now(), TRUE);

-- User 6
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('685ac7f5-8e9f-462a-f76d-a24bd3a5fd08', 'User6', 'Test', 'user6@test.com', 'password', '+1234567895', now(),
        now(), TRUE);

-- User 7
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('1e854f3a-a942-4aff-b6bc-4ffddf590394', 'User7', 'Test', 'user7@test.com', 'password', '+1234567896', now(),
        now(), TRUE);

-- User 8
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('b5065ba4-79dd-4a19-a213-5d4767430234', 'User8', 'Test', 'user8@test.com', 'password', '+1234567897', now(),
        now(), TRUE);

-- User 9
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('1e3e9e3b-c8c4-497c-acb6-bf69587a2046', 'User9', 'Test', 'user9@test.com', 'password', '+1234567898', now(),
        now(), TRUE);

-- User 10
INSERT INTO users (id, first_name, last_name, email, password, phone_number, created_at, updated_at, email_confirmed)
VALUES ('63a89622-07d4-4797-9320-6159f6569e70', 'User10', 'Test', 'user10@test.com', 'password', '+1234567899', now(),
        now(), TRUE);

-- Define 10 random client IDs
-- Client 1
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 2
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'c45ac7f5-4e5f-462a-b76d-624bd3a5fd04', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 3
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'd55ac7f5-5e6f-462a-c76d-724bd3a5fd05', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 4
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'e65ac7f5-6e7f-462a-d76d-824bd3a5fd06', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 5
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'f75ac7f5-7e8f-462a-e76d-924bd3a5fd07', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 6
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), '685ac7f5-8e9f-462a-f76d-a24bd3a5fd08', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 7
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), '1e854f3a-a942-4aff-b6bc-4ffddf590394', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 8
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'b5065ba4-79dd-4a19-a213-5d4767430234', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 9
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), '1e3e9e3b-c8c4-497c-acb6-bf69587a2046', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Client 10
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), '63a89622-07d4-4797-9320-6159f6569e70', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100,
        '2100-01-01', 'PAYMENT', now());

-- Insert 10 visits for the max capacity test, one for each client, same schedule and date
-- Visit 1
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', '2099-12-27',
        now(), now());

-- Visit 2
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'c45ac7f5-4e5f-462a-b76d-624bd3a5fd04', '2099-12-27',
        now(), now());

-- Visit 3
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'd55ac7f5-5e6f-462a-c76d-724bd3a5fd05', '2099-12-27',
        now(), now());

-- Visit 4
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'e65ac7f5-6e7f-462a-d76d-824bd3a5fd06', '2099-12-27',
        now(), now());

-- Visit 5
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'f75ac7f5-7e8f-462a-e76d-924bd3a5fd07', '2099-12-27',
        now(), now());

-- Visit 6
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', '685ac7f5-8e9f-462a-f76d-a24bd3a5fd08', '2099-12-27',
        now(), now());

-- Visit 7
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', '1e854f3a-a942-4aff-b6bc-4ffddf590394', '2099-12-27',
        now(), now());

-- Visit 8
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', 'b5065ba4-79dd-4a19-a213-5d4767430234', '2099-12-27',
        now(), now());

-- Visit 9
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', '1e3e9e3b-c8c4-497c-acb6-bf69587a2046', '2099-12-27',
        now(), now());

-- Visit 10
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES (gen_random_uuid(), '9a7632b1-e932-48fd-9296-001036b4ec19', '63a89622-07d4-4797-9320-6159f6569e70', '2099-12-27',
        now(), now());

-- Insert a visit in the past (for cancel tests)
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES ('a1a1a1a1-e932-48fd-9296-001036b4ec19', '9a7632b1-e932-48fd-9296-001036b4ec19',
        'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', '2020-01-01', now(), now());

-- Insert a visit less than 4 hours from now (for cancel tests)
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES ('b2b2b2b2-e932-48fd-9296-001036b4ec19', '9a7632b1-e932-48fd-9296-001036b4ec19',
        'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', CURRENT_DATE, now(), now());

-- Insert a visit that can be cancelled (more than 4 hours from now) (for cancel tests)
INSERT INTO client_training_visits (id, schedule_id, client_id, date, created_at, updated_at)
VALUES ('c3c3c3c3-e932-48fd-9296-001036b4ec19', '9a7632b1-e932-48fd-9296-001036b4ec19',
        'a35ac7f5-3e4f-462a-a76d-524bd3a5fd01', CURRENT_DATE + INTERVAL '2 days', now(), now());
