INSERT INTO roles(id, name)
VALUES ('92395bf1-a253-4b79-b0eb-969863d62a6f', 'ADMIN'),
       ('ef32d172-df6f-4a19-8eff-ae70ac6852d7', 'COACH'),
       ('e90a9385-08cb-4489-8191-7c0e1de82b7d', 'CLIENT')
ON CONFLICT DO NOTHING;