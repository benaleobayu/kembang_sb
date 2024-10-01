package com.bca.byc.repository;
import com.bca.byc.entity.AppUserRequestContact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRequestContactRepository extends JpaRepository<AppUserRequestContact, Long> {
    // Custom query method to find notification settings by appUserId
    Optional<AppUserRequestContact> findByAppUserId(Long appUserId);
}
