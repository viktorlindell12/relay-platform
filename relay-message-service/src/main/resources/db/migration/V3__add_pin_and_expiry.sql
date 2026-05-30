ALTER TABLE messages
    ADD COLUMN pinned     BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN expires_at TIMESTAMPTZ;

-- Back-fill expiry for all existing messages (24 h after creation)
UPDATE messages SET expires_at = created_at + INTERVAL '24 hours';

-- Partial index used by the cleanup job — only unpinned rows have an expiry deadline
CREATE INDEX messages_expires_at_idx ON messages (expires_at)
    WHERE pinned = FALSE;