package com.ntp.sasin_be.auth.controllers;

import com.ntp.sasin_be.auth.dto.ChangePasswordRequest;
import com.ntp.sasin_be.auth.services.ChangePasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class ChangePasswordController {
    @Autowired
    private ChangePasswordService changePasswordService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        try {
            changePasswordService.changePassword(changePasswordRequest, principal.getName());
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
