-- Replace numeric channel_id with a human-readable channel name (e.g. "general")
ALTER TABLE messages DROP COLUMN channel_id;
DROP INDEX IF EXISTS messages_channel_id_idx;

-- Add as nullable first, then enforce NOT NULL — safe pattern on non-empty tables
ALTER TABLE messages ADD COLUMN channel VARCHAR(100);
ALTER TABLE messages ALTER COLUMN channel SET NOT NULL;

CREATE INDEX messages_channel_idx ON messages (channel);