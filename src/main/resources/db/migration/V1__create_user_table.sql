-- V1__create_users_table.sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create the users table with UUID primary key
CREATE TABLE IF NOT EXISTS users (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      username VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL UNIQUE,
      password VARCHAR(255) NOT NULL,
      role VARCHAR(50) NOT NULL,
      level INTEGER DEFAULT 0,
      score INTEGER DEFAULT 0,
      day_streak INTEGER DEFAULT 0,
      count_test INTEGER DEFAULT 0
);
