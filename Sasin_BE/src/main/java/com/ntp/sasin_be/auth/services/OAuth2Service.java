package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import com.ntp.sasin_be.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service {
    @Autowired
    private UserDetailRepository userDetailRepository;

    // Lấy user từ database theo email
    public User getUser(String email) {
        return userDetailRepository.findByEmail(email);
    }

    // Tạo user mới từ OAuth2 nếu chưa tồn tại
    public User createUser(OAuth2User oAuth2User, String provider) {
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String email = oAuth2User.getAttribute("email");
        String providerId = oAuth2User.getName(); // id duy nhất từ Google/Facebook/GitHub

        User existingUser = userDetailRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        }

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .fullName(firstName + " " + lastName)
                .email(email)
                .enabled(true)
                .role(Role.USER)
                .provider(provider.toUpperCase())
                .providerId(providerId)
                .build();

        return userDetailRepository.save(newUser);
    }
}
