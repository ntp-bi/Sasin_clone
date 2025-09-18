package com.ntp.sasin_be.repositories;

import com.ntp.sasin_be.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
