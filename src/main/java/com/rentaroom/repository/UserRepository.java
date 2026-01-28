package com.rentaroom.repository;

import com.rentaroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Analytics queries
    List<User> findByRole(User.Role role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate")
    long countByCreatedAtAfter(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.createdAt >= :startDate")
    long countByRoleAndCreatedAtAfter(@Param("role") User.Role role, @Param("startDate") LocalDateTime startDate);
    
    List<User> findAll();
}
