package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
