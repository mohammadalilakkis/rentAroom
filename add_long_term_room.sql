-- SQL script to add a new room with long-term validity
-- This room will be available from today until December 31, 2028 (approximately 3+ years)

USE rent_a_room;

-- First, check if you have a host user. If not, create one:
-- INSERT INTO users (name, email, password, phone, role) 
-- VALUES ('Long Term Host', 'host@example.com', 'hashed_password', '5551234567', 'host');

-- Add a new room with long-term availability
-- Replace host_id with an actual host user ID from your users table
INSERT INTO rooms (host_id, title, description, location, price, capacity, available_from, available_to, beds_available)
VALUES
(
    1,  -- Change this to your actual host user ID
    'Premium Long-Term Rental Suite',
    'Beautifully designed suite perfect for extended stays. Features include:
    - Fully furnished with modern amenities
    - High-speed WiFi and smart TV
    - Fully equipped kitchen with appliances
    - Private bathroom with premium fixtures
    - Comfortable workspace area
    - Laundry facilities available
    - Secure building with 24/7 access
    - Close to public transportation and shopping centers
    
    Ideal for professionals, students, or anyone seeking a comfortable long-term accommodation solution.',
    'New York',
    175.00,
    2,
    CURDATE(),  -- Available from today
    '2028-12-31',  -- Available until end of 2028 (long-term validity)
    2  -- Number of beds available
);

-- Verify the insertion
SELECT 
    id,
    title,
    location,
    price,
    capacity,
    available_from,
    available_to,
    DATEDIFF(available_to, available_from) AS days_available
FROM rooms
WHERE title = 'Premium Long-Term Rental Suite';
