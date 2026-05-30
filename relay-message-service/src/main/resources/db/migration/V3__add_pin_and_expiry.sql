ALTER TABLE messages
    ADD COLUMN pinned     BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN expires_at TIMESTAMPTZ;

-- Back-fill expiry for all existing messages (24 h after creation)
UPDATE messages SET expires_at = created_at + INTERVAL '24 hours';

-- Enforce the pin/expiry invariant: pinned rows must have no deadline,
-- unpinned rows must always have a deadline.
ALTER TABLE messages ADD CONSTRAINT chk_pin_expiry CHECK (
    (pinned = TRUE  AND expires_at IS NULL) OR
    (pinned = FALSE AND expires_at IS NOT NULL)
);

-- Partial index used by the cleanup job — only unpinned rows have an expiry deadline
CREATE INDEX messages_expires_at_idx ON messages (expires_at)
    WHERE pinned = FALSE;