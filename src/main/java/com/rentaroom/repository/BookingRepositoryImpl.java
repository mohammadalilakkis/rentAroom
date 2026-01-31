package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    private final EntityManager entityManager;

    public BookingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BigDecimal sumTotalPriceByConfirmedStatus() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Booking> root = query.from(Booking.class);

        query.select(cb.sum(root.get("totalPrice")));
        query.where(cb.equal(root.get("status"), Booking.BookingStatus.CONFIRMED));

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return result;
    }

    @Override
    public BigDecimal sumTotalPriceByConfirmedStatusAndCreatedAtAfter(LocalDateTime startDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Booking> root = query.from(Booking.class);

        query.select(cb.sum(root.get("totalPrice")));
        query.where(
            cb.equal(root.get("status"), Booking.BookingStatus.CONFIRMED),
            cb.greaterThanOrEqualTo(root.get("createdAt"), startDate)
        );

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return result;
    }
}
