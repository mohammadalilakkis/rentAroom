package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.repository.specification.BookingSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking>, BookingRepositoryCustom {
    List<Booking> findByRenter(User renter);
    List<Booking> findByRoom(Room room);

    default List<Booking> findConflictingBookings(Room room, LocalDate checkIn, LocalDate checkOut) {
        return findAll(BookingSpecifications.findConflictingBookings(room, checkIn, checkOut));
    }

    long countByStatus(Booking.BookingStatus status);

    default long countByCreatedAtAfter(LocalDateTime startDate) {
        return count(BookingSpecifications.countByCreatedAtAfter(startDate));
    }

    default long countByStatusAndCreatedAtAfter(Booking.BookingStatus status, LocalDateTime startDate) {
        return count(BookingSpecifications.countByStatusAndCreatedAtAfter(status, startDate));
    }

    List<Booking> findAll();
}
