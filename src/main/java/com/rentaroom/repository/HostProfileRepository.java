package com.rentaroom.repository;

import com.rentaroom.model.HostProfile;
import com.rentaroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
    Optional<HostProfile> findByUser(User user);
    boolean existsByUser(User user);
}
