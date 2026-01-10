package com.rentaroom.converter;

import com.rentaroom.model.Booking;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<Booking.BookingStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(Booking.BookingStatus status) {
        if (status == null) {
            return null;
        }
        return status.name().toLowerCase();
    }
    
    @Override
    public Booking.BookingStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Booking.BookingStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle case-insensitive conversion
            if (dbData.equalsIgnoreCase("pending")) {
                return Booking.BookingStatus.PENDING;
            } else if (dbData.equalsIgnoreCase("confirmed")) {
                return Booking.BookingStatus.CONFIRMED;
            } else if (dbData.equalsIgnoreCase("cancelled")) {
                return Booking.BookingStatus.CANCELLED;
            }
            throw e;
        }
    }
}
