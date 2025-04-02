INSERT INTO payment_tariffs (id, tenant_id, name, price, currency, valid_days, trainings_count, created_at, updated_at)
VALUES ('c35ac7f5-3e4f-462a-a76d-524bd3a5fd01', '7a7632b1-e932-48fd-9296-001036b4ec19', 'Basic monthly subscription',
        29.99, 'USD', 30, 5, NOW(), NOW()),
       ('d45ac7f5-3e4f-462a-a76d-524bd3a5fd02', '7a7632b1-e932-48fd-9296-001036b4ec19', 'Premium annual subscription',
        299.99, 'USD', 365, 10, NOW(), NOW());