CREATE TABLE user_profiles
(
    id           BIGSERIAL    PRIMARY KEY,
    auth_user_id BIGINT       NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL
);