package com.rentaroom.service;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Payment;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.repository.BookingRepository;
import com.rentaroom.repository.PaymentRepository;
import com.rentaroom.repository.RoomRepository;
import com.rentaroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    // Overview Statistics
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalRooms = roomRepository.count();
        long totalBookings = bookingRepository.count();
        
        BigDecimal totalRevenue = paymentRepository.sumAmountByPaidStatus();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalRooms", totalRooms);
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        
        // Users by role
        List<User> allUsersForStats = userRepository.findAll();
        long adminCount = allUsersForStats.stream().filter(u -> u.getRole() == User.Role.ADMIN).count();
        long hostCount = allUsersForStats.stream().filter(u -> u.getRole() == User.Role.HOST).count();
        long renterCount = allUsersForStats.stream().filter(u -> u.getRole() == User.Role.RENTER).count();
        
        stats.put("adminCount", adminCount);
        stats.put("hostCount", hostCount);
        stats.put("renterCount", renterCount);
        
        // Bookings by status
        long pendingBookings = bookingRepository.countByStatus(Booking.BookingStatus.PENDING);
        long confirmedBookings = bookingRepository.countByStatus(Booking.BookingStatus.CONFIRMED);
        long cancelledBookings = bookingRepository.countByStatus(Booking.BookingStatus.CANCELLED);
        
        stats.put("pendingBookings", pendingBookings);
        stats.put("confirmedBookings", confirmedBookings);
        stats.put("cancelledBookings", cancelledBookings);
        
        return stats;
    }
    
    // User Analytics
    public Map<String, Object> getUserAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        
        long totalUsers = userRepository.count();
        long usersLast30Days = userRepository.countByCreatedAtAfter(thirtyDaysAgo);
        long usersLast7Days = userRepository.countByCreatedAtAfter(sevenDaysAgo);
        
        analytics.put("totalUsers", totalUsers);
        analytics.put("usersLast30Days", usersLast30Days);
        analytics.put("usersLast7Days", usersLast7Days);
        
        // Users by role
        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
        List<User> hosts = userRepository.findByRole(User.Role.HOST);
        List<User> renters = userRepository.findByRole(User.Role.RENTER);
        
        analytics.put("adminCount", admins.size());
        analytics.put("hostCount", hosts.size());
        analytics.put("renterCount", renters.size());
        
        // Recent registrations (last 10)
        List<User> allUsers = userRepository.findAll();
        List<User> recentUsers = allUsers.stream()
            .filter(u -> u.getCreatedAt() != null)
            .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
            .limit(10)
            .collect(Collectors.toList());
        
        analytics.put("recentUsers", recentUsers);
        
        return analytics;
    }
    
    // Room Analytics
    public Map<String, Object> getRoomAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        long totalRooms = roomRepository.count();
        BigDecimal avgPrice = roomRepository.getAveragePrice();
        if (avgPrice == null) avgPrice = BigDecimal.ZERO;
        
        analytics.put("totalRooms", totalRooms);
        analytics.put("averagePrice", avgPrice.setScale(2, RoundingMode.HALF_UP));
        
        // Rooms by location
        List<Object[]> locationCounts = roomRepository.countRoomsByLocation();
        Map<String, Long> roomsByLocation = new LinkedHashMap<>();
        for (Object[] row : locationCounts) {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminService.java:row\",\"message\":\"countRoomsByLocation row\",\"data\":{\"row1Class\":\"" + (row[1] != null ? row[1].getClass().getName() : "null") + "\"},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"C\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            String location = (String) row[0];
            Long count = (Long) row[1];
            roomsByLocation.put(location != null ? location : "Unknown", count);
        }
        analytics.put("roomsByLocation", roomsByLocation);
        
        // Most popular locations (top 5)
        List<Map<String, Object>> topLocations = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Long> entry : roomsByLocation.entrySet()) {
            if (count >= 5) break;
            Map<String, Object> locationData = new HashMap<>();
            locationData.put("location", entry.getKey());
            locationData.put("count", entry.getValue());
            topLocations.add(locationData);
            count++;
        }
        analytics.put("topLocations", topLocations);
        
        return analytics;
    }
    
    // Booking Analytics
    public Map<String, Object> getBookingAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        
        long totalBookings = bookingRepository.count();
        long bookingsLast30Days = bookingRepository.countByCreatedAtAfter(thirtyDaysAgo);
        long bookingsLast7Days = bookingRepository.countByCreatedAtAfter(sevenDaysAgo);
        
        analytics.put("totalBookings", totalBookings);
        analytics.put("bookingsLast30Days", bookingsLast30Days);
        analytics.put("bookingsLast7Days", bookingsLast7Days);
        
        // Bookings by status
        long pending = bookingRepository.countByStatus(Booking.BookingStatus.PENDING);
        long confirmed = bookingRepository.countByStatus(Booking.BookingStatus.CONFIRMED);
        long cancelled = bookingRepository.countByStatus(Booking.BookingStatus.CANCELLED);
        
        analytics.put("pendingBookings", pending);
        analytics.put("confirmedBookings", confirmed);
        analytics.put("cancelledBookings", cancelled);
        
        // Revenue from bookings
        BigDecimal totalRevenue = bookingRepository.sumTotalPriceByConfirmedStatus();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        
        BigDecimal revenueLast30Days = bookingRepository.sumTotalPriceByConfirmedStatusAndCreatedAtAfter(thirtyDaysAgo);
        if (revenueLast30Days == null) revenueLast30Days = BigDecimal.ZERO;
        
        analytics.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
        analytics.put("revenueLast30Days", revenueLast30Days.setScale(2, RoundingMode.HALF_UP));
        
        // Average booking value
        BigDecimal avgBookingValue = confirmed > 0 
            ? totalRevenue.divide(BigDecimal.valueOf(confirmed), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        analytics.put("averageBookingValue", avgBookingValue);
        
        return analytics;
    }
    
    // Top Performers
    public Map<String, Object> getTopPerformers() {
        Map<String, Object> performers = new HashMap<>();
        
        // Top hosts by bookings
        List<Booking> allBookings = bookingRepository.findAll();
        Map<User, Long> hostBookingCounts = allBookings.stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
            .filter(b -> b.getRoom() != null && b.getRoom().getHost() != null)
            .collect(Collectors.groupingBy(
                b -> b.getRoom().getHost(),
                Collectors.counting()
            ));
        
        List<Map<String, Object>> topHostsByBookings = hostBookingCounts.entrySet().stream()
            .sorted(Map.Entry.<User, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Map<String, Object> hostData = new HashMap<>();
                hostData.put("host", entry.getKey());
                hostData.put("bookingCount", entry.getValue());
                return hostData;
            })
            .collect(Collectors.toList());
        
        performers.put("topHostsByBookings", topHostsByBookings);
        
        // Top hosts by revenue
        Map<User, BigDecimal> hostRevenue = allBookings.stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
            .filter(b -> b.getRoom() != null && b.getRoom().getHost() != null)
            .collect(Collectors.groupingBy(
                b -> b.getRoom().getHost(),
                Collectors.reducing(BigDecimal.ZERO, Booking::getTotalPrice, BigDecimal::add)
            ));
        
        List<Map<String, Object>> topHostsByRevenue = hostRevenue.entrySet().stream()
            .sorted(Map.Entry.<User, BigDecimal>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Map<String, Object> hostData = new HashMap<>();
                hostData.put("host", entry.getKey());
                hostData.put("revenue", entry.getValue().setScale(2, RoundingMode.HALF_UP));
                return hostData;
            })
            .collect(Collectors.toList());
        
        performers.put("topHostsByRevenue", topHostsByRevenue);
        
        // Top rooms by bookings
        Map<Room, Long> roomBookingCounts = allBookings.stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
            .collect(Collectors.groupingBy(
                Booking::getRoom,
                Collectors.counting()
            ));
        
        List<Map<String, Object>> topRoomsByBookings = roomBookingCounts.entrySet().stream()
            .sorted(Map.Entry.<Room, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("room", entry.getKey());
                roomData.put("bookingCount", entry.getValue());
                return roomData;
            })
            .collect(Collectors.toList());
        
        performers.put("topRoomsByBookings", topRoomsByBookings);
        
        return performers;
    }
    
    // Financial Analytics
    public Map<String, Object> getFinancialAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        // Total revenue from payments
        BigDecimal totalRevenue = paymentRepository.sumAmountByPaidStatus();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        
        analytics.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
        
        // Revenue by month (last 6 months)
        List<Map<String, Object>> revenueByMonth = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDateTime monthStart = LocalDateTime.now().minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            
            BigDecimal monthRevenue = paymentRepository.sumAmountByPaidStatusAndPaidAtAfter(monthStart);
            if (monthRevenue == null) monthRevenue = BigDecimal.ZERO;
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("key", monthStart.getMonth().toString() + " " + monthStart.getYear());
            monthData.put("value", monthRevenue.setScale(2, RoundingMode.HALF_UP));
            revenueByMonth.add(monthData);
        }
        
        analytics.put("revenueByMonth", revenueByMonth);
        
        // Average booking value
        List<Payment> paidPayments = paymentRepository.findByStatus(Payment.PaymentStatus.PAID);
        BigDecimal avgBookingValue = paidPayments.isEmpty() 
            ? BigDecimal.ZERO
            : totalRevenue.divide(BigDecimal.valueOf(paidPayments.size()), 2, RoundingMode.HALF_UP);
        
        analytics.put("averageBookingValue", avgBookingValue);
        
        return analytics;
    }
    
    // Get all users for management
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get all rooms for management
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    // Get all bookings for management
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    // Get all payments for management
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
