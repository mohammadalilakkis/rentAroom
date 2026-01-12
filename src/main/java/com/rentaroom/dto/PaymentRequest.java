package com.rentaroom.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod;
    
    // Stripe payment intent ID (created on frontend)
    private String paymentIntentId;
    
    public PaymentRequest() {}
    
    public PaymentRequest(Long bookingId, String paymentMethod, String paymentIntentId) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.paymentIntentId = paymentIntentId;
    }
    
    public Long getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentIntentId() {
        return paymentIntentId;
    }
    
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }
}
