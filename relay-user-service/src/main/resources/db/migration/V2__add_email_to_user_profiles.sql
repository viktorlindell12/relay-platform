ALTER TABLE user_profiles ADD COLUMN email VARCHAR(255);
ALTER TABLE user_profiles ADD CONSTRAINT user_profiles_email_key UNIQUE (email);
-- Enforce NOT NULL after unique constraint — fails fast in production if rows need backfilling
ALTER TABLE user_profiles ALTER COLUMN email SET NOT NULL;