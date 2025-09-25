package com.ntp.sasin_be.auth.mapper;

import com.ntp.sasin_be.auth.dto.UserDetailsDTO;
import com.ntp.sasin_be.auth.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDetailsDTO mapToDTO(User user) {
        return UserDetailsDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.isGender())
                .image(user.getImage())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();
    }
}
