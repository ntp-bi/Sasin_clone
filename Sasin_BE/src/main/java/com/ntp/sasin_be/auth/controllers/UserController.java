package com.ntp.sasin_be.auth.controllers;

import com.ntp.sasin_be.auth.dto.UserDetailsDTO;
import com.ntp.sasin_be.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<UserDetailsDTO> getUserProfile(Principal principal) {
        UserDetailsDTO userDetailsDTO = userService.getUserProfile(principal);
        return ResponseEntity.ok(userDetailsDTO);
    }

    @GetMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable Long id) {
        UserDetailsDTO userDetailsDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDetailsDTO);
    }

    @PutMapping(value = "/user/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDetailsDTO> updateUserProfile(@RequestPart("user") UserDetailsDTO userDetailsDTO,
                                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                                            Principal principal) {
        return ResponseEntity.ok(userService.updateUserProfile(userDetailsDTO, image, principal));
    }

    @PutMapping(value = "/admin/user/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDTO> updateUserByAdmin(@RequestPart("user") UserDetailsDTO userDetailsDTO,
                                                            @RequestPart(value = "image", required = false) MultipartFile file,
                                                            @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(userDetailsDTO, file, id));
    }

    @DeleteMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDetailsDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
