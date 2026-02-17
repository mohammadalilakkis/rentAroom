package com.rentaroom.repository;

import com.rentaroom.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final EntityManager entityManager;

    public PaymentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BigDecimal sumAmountByPaidStatus() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Payment> root = query.from(Payment.class);

        query.select(cb.sum(root.get("amount")));
        query.where(cb.equal(root.get("status"), Payment.PaymentStatus.PAID));

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return result;
    }

    @Override
    public BigDecimal sumAmountByPaidStatusAndPaidAtAfter(LocalDateTime startDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Payment> root = query.from(Payment.class);

        query.select(cb.sum(root.get("amount")));
        query.where(
            cb.equal(root.get("status"), Payment.PaymentStatus.PAID),
            cb.greaterThanOrEqualTo(root.get("paidAt"), startDate)
        );

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return result;
    }
}
