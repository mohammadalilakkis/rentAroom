package com.rentaroom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.rentaroom.converter.PaymentStatusConverter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @NotNull(message = "Amount is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "stripe_payment_intent_id", length = 255)
    private String stripePaymentIntentId;
    
    @Column(name = "stripe_transaction_id", length = 255)
    private String stripeTransactionId;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @Convert(converter = PaymentStatusConverter.class)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @PrePersist
    protected void onCreate() {
        if (paidAt == null && status == PaymentStatus.PAID) {
            paidAt = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { 
        this.status = status;
        if (status == PaymentStatus.PAID && paidAt == null) {
            paidAt = LocalDateTime.now();
        }
    }
    
    public String getStripePaymentIntentId() { return stripePaymentIntentId; }
    public void setStripePaymentIntentId(String stripePaymentIntentId) { this.stripePaymentIntentId = stripePaymentIntentId; }
    
    public String getStripeTransactionId() { return stripeTransactionId; }
    public void setStripeTransactionId(String stripeTransactionId) { this.stripeTransactionId = stripeTransactionId; }
    
    public enum PaymentStatus {
        PENDING, PAID, FAILED
    }
}
