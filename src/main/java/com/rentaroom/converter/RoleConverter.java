package com.rentaroom.converter;

import com.rentaroom.model.User;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<User.Role, String> {
    
    @Override
    public String convertToDatabaseColumn(User.Role role) {
        if (role == null) {
            return null;
        }
        return role.name().toLowerCase();
    }
    
    @Override
    public User.Role convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return User.Role.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle old lowercase values
            if (dbData.equalsIgnoreCase("admin")) {
                return User.Role.ADMIN;
            } else if (dbData.equalsIgnoreCase("host")) {
                return User.Role.HOST;
            } else if (dbData.equalsIgnoreCase("renter")) {
                return User.Role.RENTER;
            }
            throw e;
        }
    }
}
