package com.ntp.sasin_be.auth.controllers;

import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.services.OAuth2Service;
import com.ntp.sasin_be.config.JWTTokenHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @GetMapping("/success")
    public void callbackOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User,
                               OAuth2AuthenticationToken authentication,
                               HttpServletResponse response) throws IOException {
        // Lấy provider: google, facebook, github...
        String provider = authentication.getAuthorizedClientRegistrationId();

        // Lấy email từ OAuth2User
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            response.sendRedirect("http://localhost:3000/login?error=missing_email");
            return;
        }

        // Kiểm tra user trong DB
        User user = oAuth2Service.getUser(email);
        if (user == null) {
            user = oAuth2Service.createUser(oAuth2User, provider);
        }

        String token = jwtTokenHelper.generateToken(user.getUsername());
        response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
    }
}
