-- V2__create_followers_table.sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE followers (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           follower_uuid UUID NOT NULL,
                           owner_uuid UUID NOT NULL,
                           CONSTRAINT fk_follower
                               FOREIGN KEY(follower_uuid)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE,
                           CONSTRAINT fk_owner
                               FOREIGN KEY(owner_uuid)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE,
                           CONSTRAINT unique_follower_owner
                               UNIQUE (follower_uuid, owner_uuid)
);
