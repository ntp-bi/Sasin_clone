package com.ntp.sasin_be.auth.controllers;

import com.ntp.sasin_be.auth.dto.LoginRequest;
import com.ntp.sasin_be.auth.dto.RegistrationRequest;
import com.ntp.sasin_be.auth.dto.RegistrationResponse;
import com.ntp.sasin_be.auth.dto.UserToken;
import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.services.RegistrationService;
import com.ntp.sasin_be.auth.services.VerifyUserService;
import com.ntp.sasin_be.config.JWTTokenHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private VerifyUserService verifyUserService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse = registrationService.createUser(registrationRequest);

        return new ResponseEntity<>(registrationResponse,
                registrationResponse.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String code = map.get("code");

        User user = (User) userDetailsService.loadUserByUsername(userName);

        if (user != null && user.getVerificationCode().equals(code)) {
            verifyUserService.verifyUser(userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Tạo đối tượng Authentication
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );

            // Xác thực
            Authentication authenticationResponse = this.authenticationManager.authenticate(authentication);

            if (authenticationResponse.isAuthenticated()) {
                User user = (User) authenticationResponse.getPrincipal();
                if (!user.isEnabled()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                // Tạo Access Token
                String accessToken = jwtTokenHelper.generateToken(user.getEmail());

                // Tạo Refresh Token
                String refreshToken = jwtTokenHelper.generateToken(user.getEmail());

                // Tính thời gian hết hạn của accessToken
                Date expiredAt = new Date(System.currentTimeMillis() + 3600_000); // 1h

                UserToken userToken = UserToken.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .expiredAt(expiredAt)
                        .build();

                return new ResponseEntity<>(userToken, HttpStatus.OK);
            }

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
