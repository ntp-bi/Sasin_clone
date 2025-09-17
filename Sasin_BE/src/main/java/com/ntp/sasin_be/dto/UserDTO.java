package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Role role;
}
