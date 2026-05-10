CREATE TABLE messages
(
    id         BIGSERIAL    PRIMARY KEY,
    sender_id  BIGINT       NOT NULL,
    channel_id BIGINT       NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL
);

CREATE INDEX messages_channel_id_idx ON messages (channel_id);
CREATE INDEX messages_sender_id_idx  ON messages (sender_id);