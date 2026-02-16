-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: host.docker.internal    Database: rent_a_room
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `renter_id` int DEFAULT NULL,
  `check_in` date DEFAULT NULL,
  `check_out` date DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `beds_booked` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `renter_id` (`renter_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`renter_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,1,2,'2025-10-10','2025-10-15',600.00,'cancelled','2025-12-27 11:49:33',NULL),(2,2,4,'2025-10-20','2025-10-25',1000.00,'confirmed','2025-12-27 11:49:33',NULL),(3,3,9,'2026-01-10','2026-01-11',180.00,'confirmed','2026-01-10 13:07:15',1),(4,3,9,'2026-01-13','2026-01-14',180.00,'pending','2026-01-12 10:08:37',1),(5,3,9,'2026-01-13','2026-01-14',180.00,'pending','2026-01-12 10:16:17',1),(6,3,10,'2026-01-27','2026-01-28',180.00,'pending','2026-01-26 20:38:48',1),(7,3,10,'2026-01-29','2026-01-31',360.00,'pending','2026-01-26 20:40:24',1),(8,3,10,'2026-01-27','2026-01-30',540.00,'pending','2026-01-26 20:44:06',1),(9,3,10,'2026-01-28','2026-01-29',180.00,'pending','2026-01-27 12:30:21',1),(10,3,10,'2026-01-27','2026-01-28',180.00,'pending','2026-01-27 14:53:36',1),(11,3,10,'2026-01-27','2026-01-28',180.00,'pending','2026-01-27 15:01:51',1),(12,3,10,'2026-01-27','2026-01-28',180.00,'pending','2026-01-27 15:11:59',1),(13,4,10,'2026-01-27','2026-01-28',175.00,'confirmed','2026-01-27 15:26:33',1),(14,4,10,'2026-01-27','2026-01-28',175.00,'confirmed','2026-01-27 15:30:43',1),(15,4,10,'2026-01-28','2026-01-29',175.00,'confirmed','2026-01-28 10:06:00',1),(16,6,10,'2026-01-28','2026-01-29',100.00,'confirmed','2026-01-28 15:17:23',2),(17,6,16,'2026-01-31','2026-02-01',50.00,'confirmed','2026-01-31 17:22:06',1);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `host_profiles`
--

DROP TABLE IF EXISTS `host_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `host_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `business_name` varchar(200) DEFAULT NULL,
  `business_type` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `zip_code` varchar(20) DEFAULT NULL,
  `description` text,
  `website` varchar(255) DEFAULT NULL,
  `business_phone` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `host_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `host_profiles`
--

LOCK TABLES `host_profiles` WRITE;
/*!40000 ALTER TABLE `host_profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `host_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` int DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `paid_at` timestamp NULL DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `stripe_payment_intent_id` varchar(255) DEFAULT NULL,
  `stripe_transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,600.00,'credit_card','2025-12-27 11:49:33','paid',NULL,NULL),(2,2,1000.00,'paypal','2025-12-27 11:49:33','pending',NULL,NULL),(3,13,175.00,'card','2026-01-27 15:27:15','paid','pi_3SuG2lAw0stATI6Q2MDeeAbr','ch_3SuG2lAw0stATI6Q25Bl5R5c'),(4,14,175.00,'card','2026-01-27 15:31:41','paid','pi_3SuG6xAw0stATI6Q48FsAstH','ch_3SuG6xAw0stATI6Q4zxM5bQS'),(5,15,175.00,'card','2026-01-28 10:06:24','paid','pi_3SuXVpAw0stATI6Q34Wcdpx7','ch_3SuXVpAw0stATI6Q3Edqkq6H'),(6,16,100.00,'card','2026-01-28 15:17:56','paid','pi_3SucNJAw0stATI6Q0rqAXh6a','ch_3SucNJAw0stATI6Q0W0txTXh'),(7,17,50.00,'card','2026-01-31 17:22:43','paid','pi_3SvjkiAw0stATI6Q0o1FxuA6','ch_3SvjkiAw0stATI6Q0omwFeQu');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `renter_id` int DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `renter_id` (`renter_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`renter_id`) REFERENCES `users` (`id`),
  CONSTRAINT `reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,2,5,'Great location and very clean!','2025-12-27 11:49:33');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` int NOT NULL AUTO_INCREMENT,
  `host_id` int DEFAULT NULL,
  `title` varchar(150) DEFAULT NULL,
  `description` text,
  `location` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `available_from` date DEFAULT NULL,
  `available_to` date DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `beds_available` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `host_id` (`host_id`),
  CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,1,'Cozy Studio Downtown','A nice small apartment in the city center.','New York',120.00,2,'2025-10-01','2025-12-31','2025-12-27 11:49:33',NULL),(2,3,'Beachside Bungalow','Relaxing bungalow near the sea.','Los Angeles',200.00,4,'2025-09-15','2025-12-31','2025-12-27 11:49:33',NULL),(3,1,'Mountain Cabin Retreat','Peaceful cabin in the mountains.','Denver',180.00,5,'2025-11-01','2026-02-01','2025-12-27 11:49:33',NULL),(4,1,'Premium Long-Term Rental Suite','Beautifully designed suite perfect for extended stays. Features include:\n    - Fully furnished with modern amenities\n    - High-speed WiFi and smart TV\n    - Fully equipped kitchen with appliances\n    - Private bathroom with premium fixtures\n    - Comfortable workspace area\n    - Laundry facilities available\n    - Secure building with 24/7 access\n    - Close to public transportation and shopping centers\n    \n    Ideal for professionals, students, or anyone seeking a comfortable long-term accommodation solution.','New York',175.00,2,'2026-01-27','2028-12-31','2026-01-27 17:25:08',2),(5,1,'Premium Long-Term Rental Suite','Beautifully designed suite perfect for extended stays. Features include:\n    - Fully furnished with modern amenities\n    - High-speed WiFi and smart TV\n    - Fully equipped kitchen with appliances\n    - Private bathroom with premium fixtures\n    - Comfortable workspace area\n    - Laundry facilities available\n    - Secure building with 24/7 access\n    - Close to public transportation and shopping centers\n    \n    Ideal for professionals, students, or anyone seeking a comfortable long-term accommodation solution.','New York',175.00,2,'2026-01-28','2028-12-31','2026-01-28 17:05:39',2),(6,15,'home',' full room','beirut,street',50.00,6,'2026-01-28','2026-05-16','2026-01-28 15:16:20',24);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Alice Johnson','alice@example.com','hashed_pw1','1234567890','host','2025-12-27 11:49:33'),(2,'Bob Smith','bob@example.com','hashed_pw2','0987654321','renter','2025-12-27 11:49:33'),(3,'Carol Lee','carol@example.com','hashed_pw3','1112223333','host','2025-12-27 11:49:33'),(4,'David Brown','david@example.com','hashed_pw4','4445556666','renter','2025-12-27 11:49:33'),(9,'mhmd','mo.ali@gmail.com','123','','renter','2026-01-10 13:01:44'),(10,'Mohammad Ali2','mo.ali2@gmail.com','123','81974942','renter','2026-01-26 20:25:07'),(12,'محمد علي عامر','mo.ali3@gmail.com','123','81974942','host','2026-01-27 12:28:57'),(13,'محمد','mo.ali6@gmail.com','123','09046287187','host','2026-01-27 12:29:45'),(14,'hassoni','hassoni@gmail.com','123','81974942','renter','2026-01-28 10:05:36'),(15,'hoster','host@gmail.com','123','70252565','host','2026-01-28 15:14:42'),(16,'Admin User','admin@rentaroom.com','admin123','555-0000','admin','2026-01-28 17:20:56');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-13 12:03:10
