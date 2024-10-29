package com.bca.byc.repository;

import com.bca.byc.entity.Account;
import com.bca.byc.entity.AdminHasAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminHasAccountRepository extends JpaRepository<AdminHasAccounts, Long> {

    void deleteByAdminId(Long id);

    @Query("SELECT a FROM AdminHasAccounts a WHERE a.admin.id = :id")
    List<AdminHasAccounts> findAccountByAdminId(Long id);
}