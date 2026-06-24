-- Schema de payments_db (PostgreSQL)

CREATE TABLE IF NOT EXISTS payments (
    id                UUID PRIMARY KEY,
    order_id          UUID NOT NULL UNIQUE,   -- referencia logica a orders-service
    customer_id       UUID NOT NULL,          -- referencia logica a users-service
    amount            NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    currency          VARCHAR(3) NOT NULL DEFAULT 'PEN',
    method            VARCHAR(20) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    receipt_number    VARCHAR(50),
    receipt_issued_at TIMESTAMPTZ,
    refund_id         UUID,
    refund_reason     VARCHAR(500),
    refunded_at       TIMESTAMPTZ,
    created_at        TIMESTAMPTZ NOT NULL,
    updated_at        TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_payments_order ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_customer ON payments(customer_id);
