BEGIN;

CREATE TABLE IF NOT EXISTS merchants (
    id BIGINT PRIMARY KEY CHECK (id > 0),
    name VARCHAR(100) NOT NULL CHECK (BTRIM(name) <> ''),
    website VARCHAR(255) NOT NULL CHECK (BTRIM(website) <> '')
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY CHECK (id > 0),
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    currency CHAR(3) NOT NULL CHECK (currency = 'RUB'),
    description TEXT NOT NULL CHECK (BTRIM(description) <> ''),
    merchant_id BIGINT NOT NULL REFERENCES merchants(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_payments_merchant_id
    ON payments(merchant_id);

INSERT INTO merchants (id, name, website)
VALUES (1, 'Учебный магазин', 'https://shop.example')
ON CONFLICT (id) DO NOTHING;

INSERT INTO payments (id, amount, currency, description, merchant_id)
VALUES
    (1002, 500.00, 'RUB', 'Оплата книги', 1),
    (1003, 2990.00, 'RUB', 'Оплата курса', 1),
    (1004, 150.00, 'RUB', 'Оплата подписки', 1)
ON CONFLICT (id) DO NOTHING;

COMMIT;
