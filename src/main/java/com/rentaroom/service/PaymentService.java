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
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    
    @PostConstruct
    public void init() {
        // #region agent log
        try {
            String logPath = ".cursor/debug.log";
            String trimmedKey = stripeSecretKey != null ? stripeSecretKey.trim() : "null";
            String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:34\",\"message\":\"@Value injection check\",\"data\":{\"keyIsNull\":%s,\"keyLength\":%d,\"keyPrefix\":\"%s\",\"keySuffix\":\"%s\",\"hasWhitespace\":%s},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A,B\"}\n",
                System.currentTimeMillis(),
                stripeSecretKey == null,
                trimmedKey.length(),
                trimmedKey.length() > 10 ? trimmedKey.substring(0, 10) : trimmedKey,
                trimmedKey.length() > 10 ? trimmedKey.substring(Math.max(0, trimmedKey.length() - 10)) : trimmedKey,
                stripeSecretKey != null && !stripeSecretKey.equals(stripeSecretKey.trim()));
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {}
        // #endregion
    }
    
    @Value("${stripe.currency:usd}")
    private String currency;
    
    /**
     * Initialize Stripe API key
     */
    private void initStripe() {
        // #region agent log
        try {
            String logPath = ".cursor/debug.log";
            String trimmedKey = stripeSecretKey != null ? stripeSecretKey.trim() : "null";
            String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:42\",\"message\":\"initStripe called\",\"data\":{\"stripeSecretKeyLength\":%d,\"stripeSecretKeyPrefix\":\"%s\",\"stripeSecretKeySuffix\":\"%s\",\"currentApiKeyIsNull\":%s,\"currentApiKeyEmpty\":%s},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\"}\n",
                System.currentTimeMillis(),
                trimmedKey.length(),
                trimmedKey.length() > 10 ? trimmedKey.substring(0, 10) : trimmedKey,
                trimmedKey.length() > 10 ? trimmedKey.substring(Math.max(0, trimmedKey.length() - 10)) : trimmedKey,
                Stripe.apiKey == null,
                Stripe.apiKey != null && Stripe.apiKey.isEmpty());
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {}
        // #endregion
        
        if (Stripe.apiKey == null || Stripe.apiKey.isEmpty()) {
            // Trim the key to remove any whitespace
            String trimmedKey = stripeSecretKey != null ? stripeSecretKey.trim() : null;
            
            // #region agent log
            try {
                String logPath = ".cursor/debug.log";
                String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:48\",\"message\":\"Setting Stripe API key\",\"data\":{\"keyLength\":%d,\"keyPrefix\":\"%s\",\"keySuffix\":\"%s\",\"keyStartsWithSk\":%s},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"B\"}\n",
                    System.currentTimeMillis(),
                    trimmedKey != null ? trimmedKey.length() : 0,
                    trimmedKey != null && trimmedKey.length() > 10 ? trimmedKey.substring(0, 10) : (trimmedKey != null ? trimmedKey : "null"),
                    trimmedKey != null && trimmedKey.length() > 10 ? trimmedKey.substring(Math.max(0, trimmedKey.length() - 10)) : (trimmedKey != null ? trimmedKey : "null"),
                    trimmedKey != null && trimmedKey.startsWith("sk_"));
                Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception e) {}
            // #endregion
            
            Stripe.apiKey = trimmedKey;
        }
    }
    
    /**
     * Create a Stripe Payment Intent for a booking
     */
    public PaymentResponse createPaymentIntent(Long bookingId) {
        // #region agent log
        try {
            String logPath = ".cursor/debug.log";
            String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:51\",\"message\":\"createPaymentIntent called\",\"data\":{\"bookingId\":%d},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\"}\n",
                System.currentTimeMillis(), bookingId);
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {}
        // #endregion
        
        initStripe();
        
        // #region agent log
        try {
            String logPath = ".cursor/debug.log";
            String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:54\",\"message\":\"After initStripe\",\"data\":{\"stripeApiKeySet\":%s,\"apiKeyLength\":%d},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                System.currentTimeMillis(),
                Stripe.apiKey != null,
                Stripe.apiKey != null ? Stripe.apiKey.length() : 0);
            Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {}
        // #endregion
        
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
            
            // #region agent log
            try {
                String logPath = ".cursor/debug.log";
                String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:75\",\"message\":\"Before PaymentIntent.create\",\"data\":{\"amountInCents\":%d,\"currency\":\"%s\",\"bookingId\":%d},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\"}\n",
                    System.currentTimeMillis(), amountInCents, currency, booking.getId());
                Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception e) {}
            // #endregion
            
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
            
            // #region agent log
            try {
                String logPath = ".cursor/debug.log";
                String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:86\",\"message\":\"PaymentIntent created successfully\",\"data\":{\"paymentIntentId\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\"}\n",
                    System.currentTimeMillis(), paymentIntent.getId());
                Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception e) {}
            // #endregion
            
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
            // #region agent log
            try {
                String logPath = ".cursor/debug.log";
                String logEntry = String.format("{\"timestamp\":%d,\"location\":\"PaymentService.java:115\",\"message\":\"StripeException caught\",\"data\":{\"errorType\":\"%s\",\"errorMessage\":\"%s\",\"errorCode\":\"%s\",\"statusCode\":%d},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A,B\"}\n",
                    System.currentTimeMillis(),
                    e.getClass().getSimpleName(),
                    e.getMessage() != null ? e.getMessage().replace("\"", "'") : "null",
                    e.getCode() != null ? e.getCode() : "null",
                    e.getStatusCode() != null ? e.getStatusCode() : 0);
                Files.write(Paths.get(logPath), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception logEx) {}
            // #endregion
            
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
