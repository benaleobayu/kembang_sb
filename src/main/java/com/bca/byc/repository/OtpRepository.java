package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.auth.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByAppUserAndOtpAndValidIsTrue(AppUser user, String otp);

    @Modifying
    @Transactional
    @Query("UPDATE Otp o SET o.valid = false WHERE o.appUser = :appUser")
    void invalidateOtpsForUser(AppUser appUser);
}
