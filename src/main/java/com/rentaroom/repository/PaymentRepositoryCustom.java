package com.rentaroom.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentRepositoryCustom {
    BigDecimal sumAmountByPaidStatus();
    BigDecimal sumAmountByPaidStatusAndPaidAtAfter(LocalDateTime startDate);
}
