package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAutocheckRepository extends JpaRepository<PreRegister, Long> {
    boolean existsByMemberBankAccount(String memberBankAccount);

    PreRegister findByMemberBankAccount(String memberBankAccount);
}