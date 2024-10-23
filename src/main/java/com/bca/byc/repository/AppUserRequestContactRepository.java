package com.bca.byc.repository;
import com.bca.byc.entity.AppUserRequestContact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRequestContactRepository extends JpaRepository<AppUserRequestContact, Long> {
    // Custom query method to find notification settings by appUserId

    @Query("SELECT a FROM AppUserRequestContact a WHERE a.user.id = :id")
    Optional<AppUserRequestContact> findByUserId(@Param("id") Long appUserId);
}
