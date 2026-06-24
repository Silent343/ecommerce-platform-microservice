-- Schema de notif_db (PostgreSQL)

CREATE TABLE IF NOT EXISTS notifications (
    id           UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,          -- referencia logica a users-service
    type         VARCHAR(30) NOT NULL,
    channel      VARCHAR(20) NOT NULL,
    title        VARCHAR(255) NOT NULL,
    message      TEXT NOT NULL,
    read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS notification_preferences (
    recipient_id     UUID PRIMARY KEY,
    enabled_channels VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notifications_recipient ON notifications(recipient_id);
