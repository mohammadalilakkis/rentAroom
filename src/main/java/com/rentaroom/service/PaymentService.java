package com.rentaroom.service;

import com.rentaroom.dto.PaymentResponse;
import com.rentaroom.model.Booking;
import com.rentaroom.model.Payment;
import com.rentaroom.repository.BookingRepository;
import com.rentaroom.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BookingService bookingService;
    
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    
    @Value("${stripe.currency:usd}")
    private String currency;
    
    /**
     * Initialize Stripe API key
     */
    private void initStripe() {
        if (Stripe.apiKey == null || Stripe.apiKey.isEmpty()) {
            Stripe.apiKey = stripeSecretKey;
        }
    }
    
    /**
     * Create a Stripe Payment Intent for a booking
     */
    public PaymentResponse createPaymentIntent(Long bookingId) {
        initStripe();
        
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return new PaymentResponse("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        // Check if payment already exists
        Optional<Payment> existingPayment = paymentRepository.findByBooking(booking);
        if (existingPayment.isPresent() && existingPayment.get().getStatus() == Payment.PaymentStatus.PAID) {
            return new PaymentResponse("Payment already completed for this booking");
        }
        
        try {
            // Convert amount to cents (Stripe uses smallest currency unit)
            long amountInCents = booking.getTotalPrice()
                .multiply(BigDecimal.valueOf(100))
                .longValue();
            
            // Create Payment Intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .setDescription("Payment for booking #" + booking.getId())
                .putMetadata("booking_id", booking.getId().toString())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            // Create or update payment record
            Payment payment;
            if (existingPayment.isPresent()) {
                payment = existingPayment.get();
            } else {
                payment = new Payment();
                payment.setBooking(booking);
                payment.setAmount(booking.getTotalPrice());
            }
            
            payment.setStripePaymentIntentId(paymentIntent.getId());
            payment.setPaymentMethod("card");
            payment.setStatus(Payment.PaymentStatus.PENDING);
            payment = paymentRepository.save(payment);
            
            // Return response with client secret
            PaymentResponse response = new PaymentResponse();
            response.setId(payment.getId());
            response.setBookingId(booking.getId());
            response.setAmount(booking.getTotalPrice());
            response.setPaymentMethod("card");
            response.setStatus(payment.getStatus().name());
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setPaymentIntentId(paymentIntent.getId());
            
            return response;
            
        } catch (StripeException e) {
            e.printStackTrace();
            return new PaymentResponse("Error creating payment intent: " + e.getMessage());
        }
    }
    
    /**
     * Confirm a payment after Stripe payment intent is confirmed on frontend
     */
    public PaymentResponse confirmPayment(String paymentIntentId) {
        initStripe();
        
        Optional<Payment> paymentOpt = paymentRepository.findByStripePaymentIntentId(paymentIntentId);
        if (paymentOpt.isEmpty()) {
            return new PaymentResponse("Payment not found");
        }
        
        Payment payment = paymentOpt.get();
        
        if (payment.getStatus() == Payment.PaymentStatus.PAID) {
            return new PaymentResponse("Payment already confirmed");
        }
        
        try {
            // Retrieve payment intent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            // Check payment status
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Payment succeeded
                payment.setStatus(Payment.PaymentStatus.PAID);
                payment.setStripeTransactionId(paymentIntent.getLatestCharge());
                payment.setPaymentMethod("card");
                payment = paymentRepository.save(payment);
                
                // Confirm the booking
                bookingService.confirmBooking(payment.getBooking().getId());
                
                PaymentResponse response = new PaymentResponse();
                response.setId(payment.getId());
                response.setBookingId(payment.getBooking().getId());
                response.setAmount(payment.getAmount());
                response.setPaymentMethod(payment.getPaymentMethod());
                response.setStatus(payment.getStatus().name());
                response.setPaymentIntentId(paymentIntentId);
                response.setPaidAt(payment.getPaidAt());
                response.setMessage("Payment confirmed successfully");
                
                return response;
            } else {
                // Payment failed or is still processing
                payment.setStatus(Payment.PaymentStatus.FAILED);
                paymentRepository.save(payment);
                
                return new PaymentResponse("Payment failed or is still processing. Status: " + paymentIntent.getStatus());
            }
            
        } catch (StripeException e) {
            e.printStackTrace();
            payment.setStatus(Payment.PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return new PaymentResponse("Error confirming payment: " + e.getMessage());
        }
    }
    
    /**
     * Get payment status for a booking
     */
    public PaymentResponse getPaymentStatus(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return new PaymentResponse("Booking not found");
        }
        
        Optional<Payment> paymentOpt = paymentRepository.findByBooking(bookingOpt.get());
        if (paymentOpt.isEmpty()) {
            return new PaymentResponse("No payment found for this booking");
        }
        
        Payment payment = paymentOpt.get();
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setBookingId(bookingId);
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus().name());
        response.setPaymentIntentId(payment.getStripePaymentIntentId());
        response.setPaidAt(payment.getPaidAt());
        
        return response;
    }
}
