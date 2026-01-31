package com.rentaroom.repository.specification;

import com.rentaroom.model.Room;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class RoomSpecifications {

    private RoomSpecifications() {}

    public static Specification<Room> locationContains(String location) {
        return (root, query, cb) -> {
            if (location == null || location.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<Room> availableOnDate(LocalDate date) {
        return (root, query, cb) -> cb.and(
            cb.or(
                cb.isNull(root.get("availableFrom")),
                cb.lessThanOrEqualTo(root.get("availableFrom"), date)
            ),
            cb.or(
                cb.isNull(root.get("availableTo")),
                cb.greaterThanOrEqualTo(root.get("availableTo"), date)
            )
        );
    }

    public static Specification<Room> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Room> findAvailableRooms(String location, LocalDate date) {
        return Specification.where(locationContains(location)).and(availableOnDate(date));
    }

    public static Specification<Room> findByMaxPrice(BigDecimal maxPrice, LocalDate date) {
        return Specification.where(priceLessThanOrEqual(maxPrice)).and(availableOnDate(date));
    }
}
