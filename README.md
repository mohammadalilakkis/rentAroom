# Rent A Room - Room & Bed Rental Platform

A Spring Boot web application for renting rooms in houses, dorms, or even individual beds in shared rooms.

## Features

- 🏠 **Room Listings**: Hosts can create and manage room listings
- 🛏️ **Bed-Level Rentals**: Support for renting individual beds in dormitory-style rooms
- 🔍 **Search & Filter**: Search rooms by location and price
- 📅 **Booking System**: Renters can book rooms/beds with date selection
- 👤 **User Authentication**: Separate roles for Hosts and Renters
- 📊 **Dashboard**: Personal dashboards for managing listings and bookings
- ⭐ **Reviews & Ratings**: Rate and review rooms (schema included)

## Tech Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL
- **Frontend**: Thymeleaf Templates
- **Styling**: Modern CSS with responsive design
- **ORM**: Spring Data JPA

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## Setup Instructions

### 1. Database Setup

First, create the database using the provided SQL file:

```sql
CREATE DATABASE IF NOT EXISTS rent_a_room;
USE rent_a_room;
-- Then run the table creation statements from database.sql
```

Or let Spring Boot create it automatically by setting `spring.jpa.hibernate.ddl-auto=update` in `application.properties`.

### 2. Configure Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rent_a_room?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### 4. Sample Data (Optional)

You can run the SQL statements from `insertion.sql` to populate sample data.

## Project Structure

```
src/
├── main/
│   ├── java/com/rentaroom/
│   │   ├── RentARoomApplication.java    # Main application class
│   │   ├── controller/                  # Web controllers
│   │   │   ├── HomeController.java
│   │   │   ├── AuthController.java
│   │   │   ├── RoomController.java
│   │   │   ├── BookingController.java
│   │   │   └── DashboardController.java
│   │   ├── model/                       # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Room.java
│   │   │   ├── Booking.java
│   │   │   ├── Payment.java
│   │   │   └── Review.java
│   │   ├── repository/                  # Data access layer
│   │   │   ├── UserRepository.java
│   │   │   ├── RoomRepository.java
│   │   │   ├── BookingRepository.java
│   │   │   └── ReviewRepository.java
│   │   └── service/                     # Business logic
│   │       ├── UserService.java
│   │       ├── RoomService.java
│   │       └── BookingService.java
│   └── resources/
│       ├── application.properties       # Configuration
│       ├── templates/                   # Thymeleaf templates
│       └── static/
│           ├── css/style.css           # Styles
│           └── js/main.js              # JavaScript
```

## Usage

### For Hosts:
1. Register with role "Host"
2. Login to your account
3. Create room listings from the dashboard
4. Manage your listings (edit/delete)
5. View bookings for your rooms

### For Renters:
1. Register with role "Renter"
2. Browse available rooms on the homepage
3. Search by location or price
4. View room details
5. Book rooms/beds with check-in and check-out dates
6. Manage your bookings from the dashboard

## Features in Detail

### Bed-Level Rental
- When creating a room, hosts can specify `bedsAvailable`
- Renters can book 1 or more beds in the same room
- Booking system checks bed availability based on existing bookings

### Booking System
- Checks for date conflicts
- Calculates total price based on nights and number of beds
- Supports pending, confirmed, and cancelled statuses

### Search & Filter
- Search by location (partial match)
- Filter by maximum price
- Shows only available rooms based on dates

## Future Enhancements

- Payment integration
- Image upload for rooms
- Email notifications
- Advanced search filters
- Review and rating system implementation
- Messaging between hosts and renters
- Map integration for location display

## License

This project is open source and available for educational purposes.
