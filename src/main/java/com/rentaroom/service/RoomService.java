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
        roomRepository.deleteById(id);
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
