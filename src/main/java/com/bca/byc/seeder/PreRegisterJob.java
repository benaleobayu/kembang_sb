package com.bca.byc.seeder;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PreRegisterJob {

    private final PreRegisterRepository repository;
    private final AppAdminRepository adminRepository;

//    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.MEMBER_SOLITAIRE, UserType.MEMBER_PRIORITY};
        AdminApprovalStatus[] approval = {AdminApprovalStatus.PENDING, AdminApprovalStatus.APPROVED, AdminApprovalStatus.OPT_APPROVED, AdminApprovalStatus.SPV_APPROVED};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);

        AppAdmin createAdmin = adminRepository.findByEmail("admin-opt@unictive.net")
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        PreRegister data = new PreRegister(
                faker.name().fullName(), // name
                faker.internet().emailAddress(), // email
                faker.phoneNumber().phoneNumber().toString(), // phone
                memberType[faker.number().numberBetween(0, 3)], // member
                userType[faker.number().numberBetween(0, 2)], // type
                faker.lorem().characters(120), // description
                cardNumber.toString(), // card number
                cinNumber.toString(), // cin number
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)), // date of birth
                cardNumber.toString(), // card number
                cinNumber.toString(), // cin number
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)), // date of birth
                faker.nation().language().toUpperCase(), // branchCode
                faker.name().firstName(), // picName
                1, // orders
                approval[faker.number().numberBetween(0, 4)] // status approval
        );

        repository.save(data);

    }

}
