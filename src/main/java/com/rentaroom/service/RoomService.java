package com.rentaroom.service;

import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Room save(Room room) {
        return roomRepository.save(room);
    }
    
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }
    
    public List<Room> findAll() {
        return roomRepository.findAll();
    }
    
    public List<Room> findByHost(User host) {
        return roomRepository.findByHost(host);
    }
    
    public List<Room> searchAvailableRooms(String location, BigDecimal maxPrice) {
        LocalDate today = LocalDate.now();
        if (location != null && !location.trim().isEmpty() && maxPrice != null) {
            return roomRepository.findByMaxPrice(maxPrice, today).stream()
                .filter(room -> room.getLocation().toLowerCase().contains(location.toLowerCase()))
                .toList();
        } else if (location != null && !location.trim().isEmpty()) {
            return roomRepository.findAvailableRooms(location, today);
        } else if (maxPrice != null) {
            return roomRepository.findByMaxPrice(maxPrice, today);
        }
        return roomRepository.findAvailableRooms("", today);
    }
    
    public void deleteById(Long id) {
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("c:\\Users\\acer\\Downloads\\rentAroom\\.cursor\\debug.log", true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"RoomService.java:52\",\"message\":\"deleteById entry\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
        
        // #region agent log
        try {
            java.util.Optional<com.rentaroom.model.Room> roomOpt = roomRepository.findById(id);
            if (roomOpt.isPresent()) {
                com.rentaroom.model.Room room = roomOpt.get();
                java.io.FileWriter fw = new java.io.FileWriter("c:\\Users\\acer\\Downloads\\rentAroom\\.cursor\\debug.log", true);
                fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\",\"location\":\"RoomService.java:58\",\"message\":\"room found\",\"data\":{\"roomId\":\"" + id + "\",\"title\":\"" + (room.getTitle() != null ? room.getTitle().replace("\"", "'") : "null") + "\",\"hasBookings\":\"" + (room.getBookings() != null && !room.getBookings().isEmpty()) + "\",\"bookingsCount\":\"" + (room.getBookings() != null ? room.getBookings().size() : 0) + "\",\"hasReviews\":\"" + (room.getReviews() != null && !room.getReviews().isEmpty()) + "\",\"reviewsCount\":\"" + (room.getReviews() != null ? room.getReviews().size() : 0) + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                fw.close();
            } else {
                java.io.FileWriter fw = new java.io.FileWriter("c:\\Users\\acer\\Downloads\\rentAroom\\.cursor\\debug.log", true);
                fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\",\"location\":\"RoomService.java:64\",\"message\":\"room not found\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                fw.close();
            }
        } catch (Exception e) {}
        // #endregion
        
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("c:\\Users\\acer\\Downloads\\rentAroom\\.cursor\\debug.log", true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"RoomService.java:70\",\"message\":\"before repository.deleteById\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
        
        roomRepository.deleteById(id);
        
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("c:\\Users\\acer\\Downloads\\rentAroom\\.cursor\\debug.log", true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"RoomService.java:78\",\"message\":\"repository.deleteById completed\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
    }
    
    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (room.getAvailableFrom() != null && checkIn.isBefore(room.getAvailableFrom())) {
            return false;
        }
        if (room.getAvailableTo() != null && checkOut.isAfter(room.getAvailableTo())) {
            return false;
        }
        return true;
    }
}
