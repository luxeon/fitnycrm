CREATE TABLE IF NOT EXISTS tenants
(
    id         uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS locations
(
    id          uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    tenant_id   uuid         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    address     VARCHAR(255),
    city        VARCHAR(100),
    state       VARCHAR(100),
    postal_code VARCHAR(20),
    country     VARCHAR(50),
    FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id                            uuid                         DEFAULT gen_random_uuid() PRIMARY KEY,
    tenant_id                     uuid,
    first_name                    VARCHAR(255)        NOT NULL,
    last_name                     VARCHAR(255)        NOT NULL,
    email                         VARCHAR(255) UNIQUE NOT NULL,
    password                      VARCHAR(255)        NOT NULL,
    phone_number                  VARCHAR(20),
    created_at                    TIMESTAMP WITHOUT TIME ZONE,
    updated_at                    TIMESTAMP WITHOUT TIME ZONE,
    email_confirmed               BOOLEAN             NOT NULL DEFAULT FALSE,
    confirmation_token            VARCHAR(255) UNIQUE,
    confirmation_token_expires_at TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE IF NOT EXISTS roles
(
    id   uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id uuid NOT NULL,
    role_id uuid NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    UNIQUE (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS clients
(
    id           uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    tenant_id    uuid         NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    UNIQUE (tenant_id, email),
    FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE IF NOT EXISTS services
(
    id               uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    tenant_id        uuid         NOT NULL,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    duration_minutes INTEGER,
    capacity         INTEGER,
    FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE IF NOT EXISTS schedule
(
    id                    uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    service_id            uuid        NOT NULL,
    location_id           uuid        NOT NULL,
    day_of_week           VARCHAR(10) NOT NULL,
    start_time            TIME        NOT NULL,
    end_time              TIME        NOT NULL,
    default_instructor_id uuid,
    FOREIGN KEY (service_id) REFERENCES services (id),
    FOREIGN KEY (location_id) REFERENCES locations (id),
    FOREIGN KEY (default_instructor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS sessions
(
    id            uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    schedule_id   uuid                        NOT NULL,
    session_date  DATE                        NOT NULL,
    instructor_id uuid,
    start_time    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedule (id),
    FOREIGN KEY (instructor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS visits
(
    id         uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    client_id  uuid NOT NULL,
    session_id uuid NOT NULL,
    FOREIGN KEY (client_id) REFERENCES users (id),
    FOREIGN KEY (session_id) REFERENCES sessions (id)
);

CREATE TABLE IF NOT EXISTS client_payments
(
    id               uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    client_id        uuid                        NOT NULL,
    payment_type     VARCHAR(20)                 NOT NULL, -- "per_visit", "subscription"
    service_id       uuid                        NOT NULL,
    amount           NUMERIC                     NOT NULL,
    valid_from       DATE,
    valid_until      DATE,
    visits_remaining INTEGER,
    payment_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES users (id),
    FOREIGN KEY (service_id) REFERENCES services (id)
);