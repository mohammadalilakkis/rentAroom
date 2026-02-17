package com.rentaroom.repository;

import com.rentaroom.model.User;
import com.rentaroom.repository.specification.UserSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByRole(User.Role role);

    default long countByCreatedAtAfter(LocalDateTime startDate) {
        return count(UserSpecifications.countByCreatedAtAfter(startDate));
    }

    default long countByRoleAndCreatedAtAfter(User.Role role, LocalDateTime startDate) {
        return count(UserSpecifications.countByRoleAndCreatedAtAfter(role, startDate));
    }

    List<User> findAll();
}
