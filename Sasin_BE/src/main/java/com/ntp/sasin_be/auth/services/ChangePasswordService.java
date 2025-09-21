package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.dto.ChangePasswordRequest;
import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(ChangePasswordRequest changePasswordRequest, String email) {
        User user = userDetailRepository.findByEmail(email);

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password does not match");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Confirm password does not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userDetailRepository.save(user);
    }
}
