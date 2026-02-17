package com.rentaroom.repository;

import java.math.BigDecimal;
import java.util.List;

public interface RoomRepositoryCustom {
    BigDecimal getAveragePrice();
    List<Object[]> countRoomsByLocation();
}
