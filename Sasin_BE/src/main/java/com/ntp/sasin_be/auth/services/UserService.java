package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.dto.UserDetailsDTO;
import com.ntp.sasin_be.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserService {
    User getCurrentUser(Principal principal);

    UserDetailsDTO getUserProfile(Principal principal);

    UserDetailsDTO getUserById(Long id);

    UserDetailsDTO updateUserProfile(UserDetailsDTO userDetailsDTO, MultipartFile file, Principal principal);

    UserDetailsDTO updateUser(UserDetailsDTO userDetailsDTO, MultipartFile file, Long userId);

    void deleteUser(Long userId);

    Page<UserDetailsDTO> getAllUsers(Pageable pageable);
}
