package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.dto.RegistrationRequest;
import com.ntp.sasin_be.auth.dto.RegistrationResponse;
import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import com.ntp.sasin_be.enums.Role;
import com.ntp.sasin_be.helper.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public RegistrationResponse createUser(RegistrationRequest registrationRequest) {
        User existingUser = userDetailRepository.findByEmail(registrationRequest.getEmail());

        if (existingUser != null) {
            return RegistrationResponse.builder().code(400).message("Email already exists!").build();
        }

        try {
            User user = new User();
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setFullName(user.getFirstName() + " " + user.getLastName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setGender(registrationRequest.isGender());
            user.setDateOfBirth(registrationRequest.getDateOfBirth());
            user.setRole(Role.USER);

            String code = VerificationCodeGenerator.generateCode();
            user.setVerificationCode(code);

            userDetailRepository.save(user);
            emailService.sendMail(user);

            return RegistrationResponse.builder().code(200).message("Registration Successful").build();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating user", e);
        }
    }
}
