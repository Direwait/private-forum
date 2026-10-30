-- Для gen_random_uuid() на PG < 13:
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id       UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(100) NOT NULL
);

CREATE TABLE refresh_tokens (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255),
    user_id     UUID         REFERENCES users(id) ON DELETE CASCADE,
    expiry_date TIMESTAMPTZ  NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE articles (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    title         VARCHAR(100) NOT NULL,
    content       TEXT         NOT NULL,
    author_id     UUID         REFERENCES users(id) ON DELETE SET NULL,
    access_level  VARCHAR(100) NOT NULL,
    article_view  INTEGER      NOT NULL DEFAULT 0,

    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tariffs (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    tier          VARCHAR(100) NOT NULL,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(400) NOT NULL,
    price         BIGINT       NOT NULL,
    currency      VARCHAR(3)   NOT NULL,
    duration_days INTEGER      NOT NULL,

    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID         NOT NULL REFERENCES users(id)   ON DELETE CASCADE,
    tariff_id     UUID         NOT NULL REFERENCES tariffs(id) ON DELETE RESTRICT,
    tracking_id   VARCHAR(255) NOT NULL UNIQUE,

    status        VARCHAR(20)  NOT NULL,
    amount        BIGINT       NOT NULL,
    currency      VARCHAR(3)   NOT NULL,
    description   VARCHAR(255),
    payer_email   VARCHAR(255),
    payer_phone   VARCHAR(20),

    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at       TIMESTAMPTZ,
    gateway_id    VARCHAR(255),
    ref_id        VARCHAR(255),
    error_message TEXT
);

CREATE TABLE subscriptions (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL REFERENCES users(id)    ON DELETE CASCADE,
    tariff_id  UUID        NOT NULL REFERENCES tariffs(id)  ON DELETE RESTRICT,
    payment_id UUID        NOT NULL REFERENCES payments(id) ON DELETE RESTRICT,
    is_active  BOOLEAN     NOT NULL DEFAULT FALSE,
    start_date TIMESTAMPTZ NOT NULL,
    end_date   TIMESTAMPTZ NOT NULL CHECK (start_date < end_date),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_expiry_date ON refresh_tokens(expiry_date);
CREATE INDEX idx_payments_user_id           ON payments(user_id);
CREATE INDEX idx_payments_gateway_id        ON payments(gateway_id);
CREATE INDEX idx_subscriptions_user_end     ON subscriptions(user_id, end_date);