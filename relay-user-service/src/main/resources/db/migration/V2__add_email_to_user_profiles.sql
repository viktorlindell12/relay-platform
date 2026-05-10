ALTER TABLE user_profiles ADD COLUMN email VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE user_profiles ALTER COLUMN email DROP DEFAULT;
ALTER TABLE user_profiles ADD CONSTRAINT user_profiles_email_key UNIQUE (email);