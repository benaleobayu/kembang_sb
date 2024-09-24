package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActionRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}