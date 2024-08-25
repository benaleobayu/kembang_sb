package com.bca.byc.repository;

import com.bca.byc.entity.TestAutocheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAutocheckRepository extends JpaRepository<TestAutocheck, Long> {
    boolean existsBySolitaireBankAccount(String solitaireBankAccount);
    boolean existsByMemberBankAccount(String memberBankAccount);

    TestAutocheck findBySolitaireBankAccount(String solitaireBankAccount);
}

