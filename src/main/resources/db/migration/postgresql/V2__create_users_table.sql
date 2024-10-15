CREATE TABLE IF NOT EXISTS migrations.user (
--     id PRIMARY KEY UUID DEFAULT uuid_generate_v4(),
--     for testing purposes, we will use an integer as the primary key
    id Integer PRIMARY KEY NOT NULL,
    login VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);