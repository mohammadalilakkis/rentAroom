-- Migration script for admin dashboard and host profiles
-- Run this if your database already exists

USE rent_a_room;

-- Create host_profiles table
CREATE TABLE IF NOT EXISTS host_profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    business_name VARCHAR(200),
    business_type VARCHAR(50),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    description TEXT,
    website VARCHAR(255),
    business_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- Add indexes for analytics queries
-- Index on bookings.created_at for time-based analytics
CREATE INDEX IF NOT EXISTS idx_bookings_created_at ON bookings(created_at);
CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings(status);

-- Index on rooms.created_at
CREATE INDEX IF NOT EXISTS idx_rooms_created_at ON rooms(created_at);
CREATE INDEX IF NOT EXISTS idx_rooms_location ON rooms(location);

-- Index on users.created_at
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Index on payments for revenue analytics
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
CREATE INDEX IF NOT EXISTS idx_payments_paid_at ON payments(paid_at);


-- SQL script to create the first admin user
USE rent_a_room;

-- Insert admin user
-- Change the email and password as needed
INSERT INTO users (name, email, password, phone, role, created_at)
VALUES (
    'Admin User',
    'admin@rentaroom.com',
    'admin123',  -- CHANGE THIS PASSWORD after first login!
    '555-0000',
    'admin',
    NOW()
);

-- Verify the admin was created
SELECT id, name, email, role, created_at 
FROM users 
WHERE role = 'admin';