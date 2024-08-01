package com.bca.byc.repository;

import com.bca.byc.entity.Otp;
import com.bca.byc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserAndOtpAndValidIsTrue(User user, String otp);

    @Modifying
    @Transactional
    @Query("UPDATE Otp o SET o.valid = false WHERE o.user = :user")
    void invalidateOtpsForUser(User user);
}
