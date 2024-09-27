package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.projection.FollowsUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserProjectionRepository extends JpaRepository<AppUser, Long> {

    Optional<FollowsUserProjection> findByEmail(String email);
}
