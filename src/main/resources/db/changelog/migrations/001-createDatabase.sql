CREATE TABLE users(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE refresh_tokens(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    token VARCHAR(255),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN DEFAULT FALSE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE articles (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    author_id UUID REFERENCES users(id) ON DELETE SET NULL,
    access_level VARCHAR(100),
    article_view INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tariffs (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    tier VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(400) NOT NULL,
    price BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    duration_days INTEGER NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id                UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id           UUID NOT NULL REFERENCES users(id)   ON DELETE CASCADE,
    tariff_id         UUID NOT NULL REFERENCES tariffs(id) ON DELETE RESTRICT,
    tracking_id       VARCHAR(255) NOT NULL UNIQUE,

    status            VARCHAR(20) NOT NULL,
    amount            BIGINT NOT NULL,
    currency          VARCHAR(3) NOT NULL,
    description       VARCHAR(255),
    payer_email       VARCHAR(255),
    payer_phone       VARCHAR(20),

    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at           TIMESTAMP WITH TIME ZONE,
    gateway_id        VARCHAR(255),
    ref_id            VARCHAR(255),
    error_message     TEXT
);

CREATE TABLE subscriptions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES users(id)    ON DELETE CASCADE,
    tariff_id  UUID NOT NULL REFERENCES tariffs(id)  ON DELETE RESTRICT,
    payment_id UUID NOT NULL REFERENCES payments(id) ON DELETE RESTRICT,
    is_active  BOOLEAN NOT NULL DEFAULT FALSE,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITH TIME ZONE NOT NULL CHECK (start_date < end_date),

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_expiry_date ON refresh_tokens(expiry_date);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_gateway_id ON payments(gateway_id);
CREATE INDEX idx_subscriptions_user_end ON subscriptions(user_id, end_date)

--CREATE TABLE users(
--  	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	name VARCHAR(100) NOT NULL UNIQUE,
--	password VARCHAR(255) NOT NULL,
--	role VARCHAR(100) NOT NULL
--);
--
--CREATE TABLE refresh_tokens(
-- 	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	refresh_token_hash VARCHAR(255),
--	user_id UUID REFERENCES users(id) ON DELETE CASCADE,
--	expiry_date TIMESTAMPTZ NOT NULL,
--	revoked BOOLEAN DEFAULT FALSE NOT NULL,
--
--	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
--	updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
--);
--
--CREATE TABLE articles (
-- 	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	title VARCHAR(100) NOT NULL,
--	content TEXT NOT NULL,
--	author_id UUID REFERENCES users(id) ON DELETE SET NULL,
--	access_level VARCHAR(100) NOT NULL,
--	article_view INTEGER NOT NULL DEFAULT 0,
--
--	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
--	updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
--);
--
--CREATE TABLE tariffs (
--	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	name VARCHAR(100) NOT NULL,
--	description VARCHAR(400) NOT NULL,
--
--	price BIGINT NOT NULL,
--	currency VARCHAR(3) NOT NULL,
--	duration_days INTEGER NOT NULL,
--
--	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
--	updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
--);
--
--CREATE TABLE orders (
-- 	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	user_id UUID REFERENCES users(id) ON DELETE CASCADE,
--	status VARCHAR(100),
--	total_amount BIGINT NOT NULL CHECK (total_amount > 0),
--	currency VARCHAR(3) NOT NULL,
--
--	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
--	updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
--);
--
--CREATE TABLE subscriptions (
--	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--	user_id   UUID NOT NULL REFERENCES users(id)   ON DELETE CASCADE,
--	tariff_id UUID NOT NULL REFERENCES tariffs(id) ON DELETE RESTRICT,
--    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
--
--	is_active BOOLEAN NOT NULL DEFAULT FALSE,
--	start_date TIMESTAMPTZ NOT NULL,
--	end_date TIMESTAMPTZ NOT NULL CHECK (start_date < end_date),
--
--	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
--	updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
--);
--
--CREATE TABLE payments (
--    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--    order_id          UUID NOT NULL REFERENCES orders(id) ON DELETE RESTRICT,
--
--    tracking_id       VARCHAR(255) NOT NULL UNIQUE,
--    status            VARCHAR(20) NOT NULL,
--
--    amount            BIGINT NOT NULL,
--    currency          VARCHAR(3) NOT NULL,
--    description       VARCHAR(255),
--
--    payer_email       VARCHAR(255),
--    payer_phone       VARCHAR(20),
--
--    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
--    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
--    paid_at           TIMESTAMPTZ,
--
--    gateway_id        VARCHAR(255),
--    ref_id            VARCHAR(255),
--    error_message     TEXT
--);
--
--CREATE INDEX idx_refresh_tokens_expiry_date ON refresh_tokens(expiry_date);
