package com.ntp.sasin_be.repositories;

import com.ntp.sasin_be.entities.RestaurantBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantBranchRepository extends JpaRepository<RestaurantBranch, Long> {
}
