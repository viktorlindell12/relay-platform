#!/bin/bash
set -e

# Creates user_db and message_db alongside the primary auth_db
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE user_db;
    CREATE DATABASE message_db;
EOSQL