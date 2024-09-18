package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAutocheckRepository extends JpaRepository<PreRegister, Long> {
    boolean existsByMemberBankAccount(String memberBankAccount);

    PreRegister findByMemberBankAccountAndStatusApproval(String memberBankAccount, AdminApprovalStatus statusApproval);
}