package com.rentaroom.repository;

import com.rentaroom.model.Review;
import com.rentaroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoom(Room room);
}
