-- Insert client training credits for the test client
INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES (gen_random_uuid(), 'b35ac7f5-3e4f-462a-a76d-524bd3a5fd03', 'ae4d661a-ed70-4e36-9caf-048ee8060290', 100, '2100-01-01', 'PAYMENT', now());