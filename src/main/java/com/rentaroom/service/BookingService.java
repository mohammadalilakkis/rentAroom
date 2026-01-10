package com.rentaroom.service;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RoomService roomService;
    
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public List<Booking> findByRenter(User renter) {
        return bookingRepository.findByRenter(renter);
    }
    
    public List<Booking> findByRoom(Room room) {
        return bookingRepository.findByRoom(room);
    }
    
    public boolean isBookingPossible(Room room, LocalDate checkIn, LocalDate checkOut, Integer bedsRequested) {
        if (!roomService.isAvailable(room, checkIn, checkOut)) {
            return false;
        }
        
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(room, checkIn, checkOut);
        int bedsBooked = conflictingBookings.stream()
            .mapToInt(b -> b.getBedsBooked() != null ? b.getBedsBooked() : 1)
            .sum();
        
        int availableBeds = room.getBedsAvailable() != null ? room.getBedsAvailable() : room.getCapacity();
        return (availableBeds - bedsBooked) >= bedsRequested;
    }
    
    public BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut, Integer beds) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) nights = 1;
        
        int bedsToUse = beds != null && beds > 0 ? beds : 1;
        return room.getPrice().multiply(BigDecimal.valueOf(nights * bedsToUse));
    }
    
    public Booking createBooking(Room room, User renter, LocalDate checkIn, LocalDate checkOut, Integer bedsRequested) {
        if (!isBookingPossible(room, checkIn, checkOut, bedsRequested != null ? bedsRequested : 1)) {
            throw new IllegalStateException("Room is not available for the selected dates");
        }
        
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setRenter(renter);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setBedsBooked(bedsRequested != null ? bedsRequested : 1);
        booking.setTotalPrice(calculateTotalPrice(room, checkIn, checkOut, bedsRequested));
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        return save(booking);
    }
    
    public void cancelBooking(Long bookingId) {
        Optional<Booking> booking = findById(bookingId);
        if (booking.isPresent()) {
            booking.get().setStatus(Booking.BookingStatus.CANCELLED);
            save(booking.get());
        }
    }
    
    public void confirmBooking(Long bookingId) {
        Optional<Booking> booking = findById(bookingId);
        if (booking.isPresent()) {
            booking.get().setStatus(Booking.BookingStatus.CONFIRMED);
            save(booking.get());
        }
    }
}
