package com.rentaroom.converter;

import com.rentaroom.model.Payment;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<Payment.PaymentStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(Payment.PaymentStatus status) {
        if (status == null) {
            return null;
        }
        return status.name().toLowerCase();
    }
    
    @Override
    public Payment.PaymentStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Payment.PaymentStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle case-insensitive conversion
            if (dbData.equalsIgnoreCase("pending")) {
                return Payment.PaymentStatus.PENDING;
            } else if (dbData.equalsIgnoreCase("paid")) {
                return Payment.PaymentStatus.PAID;
            } else if (dbData.equalsIgnoreCase("failed")) {
                return Payment.PaymentStatus.FAILED;
            }
            throw e;
        }
    }
}
