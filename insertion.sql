-- ================================================
-- 2️⃣ INSERT SAMPLE DATA (Lebanon)
-- ================================================

-- Users
INSERT INTO users (name, email, password, phone, role)
VALUES
('Rania Khoury', 'rania@example.com', 'hashed_pw1', '+961 3 123 456', 'host'),
('Omar Fakhry', 'omar@example.com', 'hashed_pw2', '+961 70 987 654', 'renter'),
('Layla Moussa', 'layla@example.com', 'hashed_pw3', '+961 1 234 567', 'host'),
('Karim Haddad', 'karim@example.com', 'hashed_pw4', '+961 76 555 123', 'renter');

-- Rooms (Lebanese locations)
INSERT INTO rooms (host_id, title, description, location, price, capacity, available_from, available_to)
VALUES
(1, 'Studio in Hamra, Beirut', 'Bright studio in the heart of Hamra. Walking distance to cafés, AUB, and the corniche. WiFi, AC, and a small balcony.', 'Beirut', 65.00, 2, '2025-10-01', '2025-12-31'),
(3, 'Sea View Apartment, Jounieh', 'Apartment with Mediterranean view in Jounieh. Near the cable car and beach. 2 bedrooms, kitchen, parking.', 'Jounieh', 95.00, 4, '2025-09-15', '2025-12-31'),
(1, 'Charming Old House in Byblos', 'Traditional stone house in the old souk area of Byblos (Jbeil). Steps from the port and ruins. AC, WiFi.', 'Byblos', 85.00, 4, '2025-11-01', '2026-02-01'),
(1, 'Modern Flat in Verdun, Beirut', 'Spacious furnished apartment in Verdun with city views. WiFi, full kitchen, parking. Ideal for long-term stays.', 'Beirut', 120.00, 3, CURDATE(), '2028-12-31'),
(3, 'Villa with Pool in Batroun', 'Beachside villa with private pool and garden in Batroun. 3 bedrooms, 2 bathrooms, terrace. Perfect for families or groups.', 'Batroun', 220.00, 6, CURDATE(), '2029-12-31'),
(1, 'Cozy Room in Achrafieh', 'Single room in a shared flat in Achrafieh. Central, safe, and close to nightlife and restaurants.', 'Beirut', 35.00, 1, CURDATE(), '2026-12-31'),
(3, 'Apartment Near Tyre Beach', 'Simple apartment a short walk from the sea in Tyre (Sour). Kitchen, AC, quiet neighborhood.', 'Tyre', 55.00, 3, CURDATE(), '2026-06-30');

-- Bookings
INSERT INTO bookings (room_id, renter_id, check_in, check_out, total_price, status)
VALUES
(1, 2, '2025-10-10', '2025-10-15', 325.00, 'confirmed'),
(2, 4, '2025-10-20', '2025-10-25', 475.00, 'pending');

-- Payments
INSERT INTO payments (booking_id, amount, payment_method, status)
VALUES
(1, 325.00, 'credit_card', 'paid'),
(2, 475.00, 'paypal', 'pending');

-- Reviews
INSERT INTO reviews (room_id, renter_id, rating, comment)
VALUES
(1, 2, 5, 'Perfect location and very clean. Would stay again!'),
(2, 4, 4, 'Amazing sea view. Batroun is beautiful.');

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