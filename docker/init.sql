-- Create database for the app
CREATE DATABASE IF NOT EXISTS rent_a_room;

-- Create app user and grant privileges
CREATE USER IF NOT EXISTS 'rentaroom'@'%' IDENTIFIED BY 'rentaroom';
GRANT ALL PRIVILEGES ON rent_a_room.* TO 'rentaroom'@'%';
FLUSH PRIVILEGES;
