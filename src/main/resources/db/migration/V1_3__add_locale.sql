ALTER TABLE tenants
    ADD COLUMN locale VARCHAR(4) DEFAULT 'en';
ALTER TABLE users
    ADD COLUMN locale VARCHAR(4) DEFAULT 'en';