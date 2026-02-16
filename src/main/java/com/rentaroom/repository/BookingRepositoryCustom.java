package com.rentaroom.repository;

import com.rentaroom.model.Booking;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface BookingRepositoryCustom {
    BigDecimal sumTotalPriceByConfirmedStatus();
    BigDecimal sumTotalPriceByConfirmedStatusAndCreatedAtAfter(LocalDateTime startDate);
}
