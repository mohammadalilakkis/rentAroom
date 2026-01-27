-- Migration script to add Stripe payment columns and admin role
-- Run this if your database already exists

USE rent_a_room;

-- Add Stripe-related columns to payments table
-- Check if columns exist first (MySQL 5.7+ syntax)
SET @dbname = DATABASE();
SET @tablename = 'payments';
SET @columnname = 'stripe_payment_intent_id';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) NULL')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET @columnname = 'stripe_transaction_id';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) NULL')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Update paid_at to allow NULL (since it should only be set when payment is completed)
ALTER TABLE payments MODIFY COLUMN paid_at TIMESTAMP NULL;

-- Add admin role to users table enum
-- Note: MySQL doesn't support direct enum modification, so we need to recreate
ALTER TABLE users MODIFY COLUMN role ENUM('admin', 'renter', 'host') DEFAULT 'renter';
