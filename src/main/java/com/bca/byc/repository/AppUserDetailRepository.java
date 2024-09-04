package com.bca.byc.repository;

import com.bca.byc.entity.AppUserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDetailRepository extends JpaRepository<AppUserDetail, Long> {
}
