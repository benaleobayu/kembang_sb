package com.bca.byc.repository;

import com.bca.byc.entity.AdminHasAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminHasAccountRepository extends JpaRepository<AdminHasAccounts, Long> {

    void deleteByAdminId(Long id);
}