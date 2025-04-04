INSERT INTO client_payments (id, tenant_id, client_id, training_id, status, trainings_count, valid_days, price,
                             currency, created_at)
VALUES ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd01', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 'COMPLETED', 10, 30, 99.99, 'USD', '2024-01-01T10:00:00Z'),
       ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd02', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd01',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 'COMPLETED', 5, 15, 49.99, 'USD', '2024-01-02T10:00:00Z'),
       ('d35ac7f5-3e4f-462a-a76d-524bd3a5fd03', '7a7632b1-e932-48fd-9296-001036b4ec19',
        'c35ac7f5-3e4f-462a-a76d-524bd3a5fd02',
        'ae4d661a-ed70-4e36-9caf-048ee8060290', 'CANCELED', 15, 25, 69.99, 'UAH', '2024-02-03T10:00:00Z');;