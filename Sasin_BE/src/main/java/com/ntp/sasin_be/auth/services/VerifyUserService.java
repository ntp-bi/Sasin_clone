package com.ntp.sasin_be.auth.services;

import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.auth.repositories.UserDetailRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyUserService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    public void verifyUser(String userName) {
        User user = userDetailRepository.findByEmail(userName);

        if (user == null) {
            throw new ServiceException("User not found: " + userName);
        }

        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}