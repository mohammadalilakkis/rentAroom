package com.rentaroom.repository;

import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHost(User host);
    
    @Query("SELECT r FROM Room r WHERE r.location LIKE %:location% " +
           "AND (r.availableFrom IS NULL OR r.availableFrom <= :date) " +
           "AND (r.availableTo IS NULL OR r.availableTo >= :date)")
    List<Room> findAvailableRooms(@Param("location") String location, @Param("date") LocalDate date);
    
    @Query("SELECT r FROM Room r WHERE r.price <= :maxPrice " +
           "AND (r.availableFrom IS NULL OR r.availableFrom <= :date) " +
           "AND (r.availableTo IS NULL OR r.availableTo >= :date)")
    List<Room> findByMaxPrice(@Param("maxPrice") java.math.BigDecimal maxPrice, @Param("date") LocalDate date);
}
