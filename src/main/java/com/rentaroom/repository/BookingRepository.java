package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenter(User renter);
    List<Booking> findByRoom(Room room);
    
    @Query("SELECT b FROM Booking b WHERE b.room = :room " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.checkIn <= :checkIn AND b.checkOut > :checkIn) OR " +
           "(b.checkIn < :checkOut AND b.checkOut >= :checkOut) OR " +
           "(b.checkIn >= :checkIn AND b.checkOut <= :checkOut))")
    List<Booking> findConflictingBookings(@Param("room") Room room, 
                                          @Param("checkIn") LocalDate checkIn, 
                                          @Param("checkOut") LocalDate checkOut);
}
