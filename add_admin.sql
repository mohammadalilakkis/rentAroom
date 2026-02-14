-- Add admin user for Rent A Room
-- Run this to create the first admin account

USE rent_a_room;

INSERT INTO users (name, email, password, phone, role)
VALUES (
    'Admin User',
    'admin@rentaroom.com',
    'admin123',
    '555-0000',
    'admin'
);
