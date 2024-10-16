-- Create the users table without a default for id
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       login VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

-- Insert initial data with explicit UUIDs
INSERT INTO users (id, login, email, password, role) VALUES
('3d6f0a4e-6af7-4e48-9c57-4b2b6a5eabc1', 'john_doe', 'john@example.com', '$2a$10$E1NRXjKZxZ1G0E/wMnYF1OIJ8O/2Ft2q3YBQqvJ86Gv8GHd06LQsq', 'ROLE_USER'),
('b2f5ff47-2a8a-47b0-ae45-45b9036b1a32', 'jane_admin', 'jane@example.com', '$2a$10$7vF4O5pG4j2wE2Rz1xVQheF8jvzqwJsTgdpEQpRfA2xJHVZ0VQkgC', 'ROLE_ADMIN');
