
ALTER TABLE users
    ADD COLUMN phone_number VARCHAR(20);

ALTER TABLE users
    ADD CONSTRAINT unique_phone_number UNIQUE (phone_number);
