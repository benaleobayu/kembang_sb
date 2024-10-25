package com.bca.byc.seeder;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Branch;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PreRegisterJob {

    private final PreRegisterRepository repository;
    private final AppAdminRepository adminRepository;
    private final BranchRepository branchRepository;

//    @Scheduled(fixedDelay = 3000)
    public void run() {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.MEMBER_SOLITAIRE, UserType.MEMBER_PRIORITY};
        AdminApprovalStatus[] approval = {AdminApprovalStatus.PENDING, AdminApprovalStatus.APPROVED, AdminApprovalStatus.OPT_APPROVED, AdminApprovalStatus.SPV_APPROVED};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);

        AppAdmin createAdmin = adminRepository.findByEmail("admin-opt@unictive.net")
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Branch branch = branchRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        PreRegister data = new PreRegister();
        data.setId(null);
        data.setName(faker.name().fullName());
        data.setEmail(faker.internet().emailAddress());
        data.setPhone(faker.phoneNumber().phoneNumber().toString());
        data.setAccountType(memberType[faker.number().numberBetween(0, 3)]);
        data.setMemberType(userType[faker.number().numberBetween(0, 2)]);
        data.setParentType(userType[faker.number().numberBetween(0, 2)]);
        data.setDescription(faker.lorem().characters(120));
        data.setMemberBankAccount(cardNumber.toString());
        data.setParentBankAccount(cardNumber.toString());
        data.setMemberCin(cinNumber.toString());
        data.setParentCin(cinNumber.toString());
        data.setMemberBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)));
        data.setParentBirthdate(LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)));
        data.setBranch(branch);
        data.setPicName(faker.name().firstName());
        data.setOrders(1);
        data.setStatusApproval(approval[faker.number().numberBetween(0, 4)]);

        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy(createAdmin);
        data.setUpdatedAt(LocalDateTime.now());

        repository.save(data);

    }

}
