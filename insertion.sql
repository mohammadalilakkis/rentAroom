-- ================================================
-- 2️⃣ INSERT SAMPLE DATA
-- ================================================

-- Users
INSERT INTO users (name, email, password, phone, role)
VALUES
('Alice Johnson', 'alice@example.com', 'hashed_pw1', '1234567890', 'host'),
('Bob Smith', 'bob@example.com', 'hashed_pw2', '0987654321', 'renter'),
('Carol Lee', 'carol@example.com', 'hashed_pw3', '1112223333', 'host'),
('David Brown', 'david@example.com', 'hashed_pw4', '4445556666', 'renter');

-- Rooms
INSERT INTO rooms (host_id, title, description, location, price, capacity, available_from, available_to)
VALUES
(1, 'Cozy Studio Downtown', 'A nice small apartment in the city center.', 'New York', 120.00, 2, '2025-10-01', '2025-12-31'),
(3, 'Beachside Bungalow', 'Relaxing bungalow near the sea.', 'Los Angeles', 200.00, 4, '2025-09-15', '2025-12-31'),
(1, 'Mountain Cabin Retreat', 'Peaceful cabin in the mountains.', 'Denver', 150.00, 5, '2025-11-01', '2026-02-01');

-- Bookings
INSERT INTO bookings (room_id, renter_id, check_in, check_out, total_price, status)
VALUES
(1, 2, '2025-10-10', '2025-10-15', 600.00, 'confirmed'),
(2, 4, '2025-10-20', '2025-10-25', 1000.00, 'pending');

-- Payments
INSERT INTO payments (booking_id, amount, payment_method, status)
VALUES
(1, 600.00, 'credit_card', 'paid'),
(2, 1000.00, 'paypal', 'pending');

-- Reviews
INSERT INTO reviews (room_id, renter_id, rating, comment)
VALUES
(1, 2, 5, 'Great location and very clean!'),
(2, 4, 4, 'Beautiful view, but a bit noisy at night.');

-- ================================================
-- 3️⃣ SELECT EXAMPLES
-- ================================================

-- All available rooms
SELECT * FROM rooms
WHERE available_from <= CURDATE() AND available_to >= CURDATE();

-- All rooms by a specific host
SELECT * FROM rooms WHERE host_id = 1;

-- Booking details with renter and room info
SELECT
    b.id AS booking_id,
    r.title AS room_title,
    u.name AS renter_name,
    b.check_in, b.check_out,
    b.total_price,
    b.status
FROM bookings b
JOIN rooms r ON b.room_id = r.id
JOIN users u ON b.renter_id = u.id;

-- Total earnings per host
SELECT
    u.name AS host_name,
    SUM(p.amount) AS total_earnings
FROM payments p
JOIN bookings b ON p.booking_id = b.id
JOIN rooms r ON b.room_id = r.id
JOIN users u ON r.host_id = u.id
WHERE p.status = 'paid'
GROUP BY u.id;

-- Average rating per room
SELECT
    r.title,
    AVG(rv.rating) AS average_rating
FROM rooms r
LEFT JOIN reviews rv ON r.id = rv.room_id
GROUP BY r.id;

-- ================================================
-- 4️⃣ UPDATE EXAMPLES
-- ================================================

-- Confirm a booking
UPDATE bookings SET status = 'confirmed' WHERE id = 2;

-- Update room price
UPDATE rooms SET price = 180.00 WHERE id = 3;

-- ================================================
-- 5️⃣ DELETE EXAMPLES
-- ================================================

-- Delete a review
DELETE FROM reviews WHERE id = 2;

-- Delete all cancelled bookings
DELETE FROM bookings WHERE status = 'cancelled';

-- ================================================
-- 6️⃣ ADVANCED / ANALYTIC QUERIES
-- ================================================

-- Top 3 most booked rooms
SELECT
    r.title,
    COUNT(b.id) AS total_bookings
FROM rooms r
JOIN bookings b ON r.id = b.room_id
GROUP BY r.id
ORDER BY total_bookings DESC
LIMIT 3;

-- Hosts with highest average rating
SELECT
    u.name AS host_name,
    AVG(rv.rating) AS avg_rating
FROM reviews rv
JOIN rooms r ON rv.room_id = r.id
JOIN users u ON r.host_id = u.id
GROUP BY u.id
ORDER BY avg_rating DESC;