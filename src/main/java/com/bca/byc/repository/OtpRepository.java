package com.bca.byc.repository;

import com.bca.byc.entity.Otp;
import com.bca.byc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserAndOtpAndValidIsTrue(User user, String otp);
}
