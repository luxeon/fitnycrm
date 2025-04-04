INSERT INTO client_training_credits (id, client_id, training_id, remaining_trainings, expires_at, trigger, created_at)
VALUES ('bded4469-b383-4bef-821f-7afb1bedd605', 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 10, '2025-05-04'::timestamp, 'PAYMENT', '2024-01-01T10:00:00Z'),
       ('44c1693f-4262-4bb8-a8dc-68c15221799c', 'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 15, '2025-05-19'::timestamp, 'PAYMENT', '2024-01-01T11:00:00Z');

INSERT INTO client_payments (id, tenant_id, client_id, training_id, credit_id, status, trainings_count, valid_days,
                             price,
                             currency, created_at)
VALUES ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd01', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 'bded4469-b383-4bef-821f-7afb1bedd605', 'COMPLETED', 10, 30, 99.99,
        'USD', '2024-01-01T10:00:00Z'),
       ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd02', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', '44c1693f-4262-4bb8-a8dc-68c15221799c', 'COMPLETED', 5, 15, 49.99,
        'USD', '2024-01-02T11:00:00Z'),
       ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd03', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd02',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', null, 'CANCELED', 15, 25, 69.99, 'UAH', '2024-02-03T10:00:00Z');;