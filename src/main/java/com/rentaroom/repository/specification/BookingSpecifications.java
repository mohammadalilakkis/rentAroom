package com.rentaroom.repository.specification;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Room;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class BookingSpecifications {

    private BookingSpecifications() {}

    public static Specification<Booking> forRoom(Room room) {
        return (root, query, cb) -> cb.equal(root.get("room"), room);
    }

    public static Specification<Booking> notCancelled() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), Booking.BookingStatus.CANCELLED);
    }

    public static Specification<Booking> conflictingWith(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, cb) -> {
            Predicate overlap1 = cb.and(
                cb.lessThanOrEqualTo(root.get("checkIn"), checkIn),
                cb.greaterThan(root.get("checkOut"), checkIn)
            );
            Predicate overlap2 = cb.and(
                cb.lessThan(root.get("checkIn"), checkOut),
                cb.greaterThanOrEqualTo(root.get("checkOut"), checkOut)
            );
            Predicate overlap3 = cb.and(
                cb.greaterThanOrEqualTo(root.get("checkIn"), checkIn),
                cb.lessThanOrEqualTo(root.get("checkOut"), checkOut)
            );
            return cb.or(overlap1, overlap2, overlap3);
        };
    }

    public static Specification<Booking> findConflictingBookings(Room room, LocalDate checkIn, LocalDate checkOut) {
        return Specification.where(forRoom(room))
            .and(notCancelled())
            .and(conflictingWith(checkIn, checkOut));
    }

    public static Specification<Booking> createdAtAfter(LocalDateTime startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
    }

    public static Specification<Booking> hasStatus(Booking.BookingStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Booking> countByCreatedAtAfter(LocalDateTime startDate) {
        return createdAtAfter(startDate);
    }

    public static Specification<Booking> countByStatusAndCreatedAtAfter(Booking.BookingStatus status, LocalDateTime startDate) {
        return Specification.where(hasStatus(status)).and(createdAtAfter(startDate));
    }
}
