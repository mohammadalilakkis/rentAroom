package com.rentaroom.repository.specification;

import com.rentaroom.model.User;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public final class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> createdAtAfter(LocalDateTime startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
    }

    public static Specification<User> hasRole(User.Role role) {
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }

    public static Specification<User> countByCreatedAtAfter(LocalDateTime startDate) {
        return createdAtAfter(startDate);
    }

    public static Specification<User> countByRoleAndCreatedAtAfter(User.Role role, LocalDateTime startDate) {
        return Specification.where(hasRole(role)).and(createdAtAfter(startDate));
    }
}
