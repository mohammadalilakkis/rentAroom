package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    // Analytics queries
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'PAID'")
    java.math.BigDecimal sumAmountByPaidStatus();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'PAID' AND p.paidAt >= :startDate")
    java.math.BigDecimal sumAmountByPaidStatusAndPaidAtAfter(@Param("startDate") LocalDateTime startDate);
    
    List<Payment> findAll();
}
