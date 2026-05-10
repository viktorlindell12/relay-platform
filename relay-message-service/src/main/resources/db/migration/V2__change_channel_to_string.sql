-- Replace numeric channel_id with a human-readable channel name (e.g. "general")
ALTER TABLE messages DROP COLUMN channel_id;
ALTER TABLE messages ADD COLUMN channel VARCHAR(100) NOT NULL;

DROP INDEX IF EXISTS messages_channel_id_idx;
CREATE INDEX messages_channel_idx ON messages (channel);