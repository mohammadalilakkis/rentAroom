package com.rentaroom.repository;

import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.repository.specification.RoomSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room>, RoomRepositoryCustom {
    List<Room> findByHost(User host);

    default List<Room> findAvailableRooms(String location, LocalDate date) {
        return findAll(RoomSpecifications.findAvailableRooms(location, date));
    }

    default List<Room> findByMaxPrice(BigDecimal maxPrice, LocalDate date) {
        return findAll(RoomSpecifications.findByMaxPrice(maxPrice, date));
    }

    List<Room> findAll();
}
