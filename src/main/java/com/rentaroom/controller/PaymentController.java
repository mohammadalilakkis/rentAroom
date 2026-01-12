package com.rentaroom.controller;

import com.rentaroom.dto.CreatePaymentIntentRequest;
import com.rentaroom.dto.PaymentResponse;
import com.rentaroom.model.User;
import com.rentaroom.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    /**
     * Create a payment intent for a booking
     * POST /api/payments/create-intent
     */
    @PostMapping("/create-intent")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @Valid @RequestBody CreatePaymentIntentRequest request,
            HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            PaymentResponse errorResponse = new PaymentResponse("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        PaymentResponse response = paymentService.createPaymentIntent(request.getBookingId());
        
        if (response.getMessage() != null && response.getMessage().contains("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Confirm a payment after Stripe processes it
     * POST /api/payments/confirm
     */
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            PaymentResponse errorResponse = new PaymentResponse("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        String paymentIntentId = request.get("paymentIntentId");
        if (paymentIntentId == null || paymentIntentId.isEmpty()) {
            PaymentResponse errorResponse = new PaymentResponse("Payment intent ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        PaymentResponse response = paymentService.confirmPayment(paymentIntentId);
        
        if (response.getMessage() != null && response.getMessage().contains("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get payment status for a booking
     * GET /api/payments/status/{bookingId}
     */
    @GetMapping("/status/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(
            @PathVariable Long bookingId,
            HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            PaymentResponse errorResponse = new PaymentResponse("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        PaymentResponse response = paymentService.getPaymentStatus(bookingId);
        
        if (response.getMessage() != null && response.getMessage().contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Webhook endpoint for Stripe to notify about payment status changes
     * POST /api/payments/webhook
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        // Note: In production, you should verify the webhook signature
        // For now, this is a basic implementation
        try {
            // Parse webhook event and update payment status accordingly
            // This is a simplified version - you should use Stripe's webhook signature verification
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }
    }
}
