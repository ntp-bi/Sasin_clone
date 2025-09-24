package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.dto.UserDetailsDTO;
import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import com.ntp.sasin_be.mapper.UserMapper;
import com.ntp.sasin_be.services.LocalUploadServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private LocalUploadServiceImpl localUploadService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getCurrentUser(Principal principal) {
        if (principal == null || principal.getName() == null) return null;
        return (User) userDetailsService.loadUserByUsername(principal.getName());
    }

    @Override
    public UserDetailsDTO getUserProfile(Principal principal) {
        User user = getCurrentUser(principal);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return userMapper.mapToDTO(user);
    }

    @Override
    public UserDetailsDTO getUserById(Long id) {
        User user = userDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDetailsDTO updateUserProfile(UserDetailsDTO userDetailsDTO, MultipartFile file, Principal principal) {
        User user = getCurrentUser(principal);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setFirstName(userDetailsDTO.getFirstName());
        user.setLastName(userDetailsDTO.getLastName());
        user.setFullName(userDetailsDTO.getFullName());
        user.setEmail(userDetailsDTO.getEmail());
        user.setGender(userDetailsDTO.isGender());
        user.setDateOfBirth(userDetailsDTO.getDateOfBirth());

        // Upload avatar nếu có
        if (file != null && !file.isEmpty()) {
            String avatarUrl = localUploadService.uploadFile(file);
            user.setImage(avatarUrl);
        }

        return userMapper.mapToDTO(userDetailRepository.save(user));
    }

    @Override
    @Transactional
    public UserDetailsDTO updateUser(UserDetailsDTO userDetailsDTO, MultipartFile file, Long userId) {
        User user = userDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDetailsDTO.getFirstName());
        user.setLastName(userDetailsDTO.getLastName());
        user.setFullName(userDetailsDTO.getFullName());
        user.setEmail(userDetailsDTO.getEmail());
        user.setGender(userDetailsDTO.isGender());
        user.setDateOfBirth(userDetailsDTO.getDateOfBirth());
        user.setRole(userDetailsDTO.getRole());

        // Upload avatar nếu có file mới
        if (file != null && !file.isEmpty()) {
            String avatarUrl = localUploadService.uploadFile(file);
            user.setImage(avatarUrl);
        }

        return userMapper.mapToDTO(userDetailRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> user = userDetailRepository.findById(userId);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        userDetailRepository.deleteById(userId);
    }

    @Override
    public Page<UserDetailsDTO> getAllUsers(Pageable pageable) {
        return userDetailRepository.findAll(pageable).map(userMapper::mapToDTO);
    }
}
