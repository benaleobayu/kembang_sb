package com.bca.byc.seeder;

import com.bca.byc.entity.*;
import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.AppUserAttributeRepository;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserJob {

    private final AppUserRepository appUserRepository;
    private final AppAdminRepository adminRepository;
    private final AppUserDetailRepository appUserDetailRepository;
    private final AppUserAttributeRepository appUserAttributeRepository;
    private final BranchRepository branchRepository;

    private final PasswordEncoder passwordEncoder;

//    @Scheduled(fixedDelay = 50)
    public void saveDataInDb() {
        Faker faker = new Faker();
        String[] statusType = {"DRAFT", "REVIEW", "REJECT", "TAKE_DOWN"};
        String[] memberType = {"Solitaire", "Priority"};
        String[] types = {"Solitaire", "Priority"};
        UserType[] userType = {UserType.MEMBER_SOLITAIRE, UserType.MEMBER_PRIORITY};
        StatusType[] statusTypes = {StatusType.REJECTED, StatusType.PRE_ACTIVATED, StatusType.ACTIVATED};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);
        String[] memberAs = {"member", "child"};

        AppAdmin admin = adminRepository.findByEmail("admin-opt@unictive.net").orElse(null);
        AppUserDetail userDetail = new AppUserDetail(
                null,
                faker.name().fullName(),
                "+62",
                faker.phoneNumber().phoneNumber(),
                cardNumber.toString(),
                cardNumber.toString(),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35)),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35)),
                cinNumber.toString(),
                cinNumber.toString(),
                null,
                faker.lorem().characters(120),
                StatusType.ACTIVATED,
                userType[faker.number().numberBetween(0, 2)],
                userType[faker.number().numberBetween(0, 2)],
                memberType[faker.number().numberBetween(0, 2)],
                "SYSTEM",
                LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)),
                faker.avatar().image(),
                faker.avatar().image(),
                memberAs[faker.number().numberBetween(0, 2)],
                getBranch(),
                faker.name().firstName(),
                faker.number().randomDigit(),
                faker.random().nextBoolean(),
                admin
        );
        AppUserDetail saveUserDetail = appUserDetailRepository.save(userDetail);

        AppUserAttribute appUserAttribute = new AppUserAttribute(
                null,
                faker.random().nextBoolean(),
                null,
                faker.random().nextBoolean(),
                null,
                faker.random().nextBoolean(),
                null,
                faker.random().nextBoolean(),
                null,
                faker.random().nextBoolean(),
                null,
                false,
                null,
                faker.random().nextBoolean(),
                null,
                faker.random().nextBoolean(),
                null,
                faker.internet().domainWord(),
                statusType[faker.random().nextInt(0, 4)]
        );
        AppUserAttribute saveUserAttribute = appUserAttributeRepository.save(appUserAttribute);

        AppUser user = new AppUser(null,
                faker.name().username(),
                faker.internet().emailAddress(),
                passwordEncoder.encode("password")
        );
        appUserRepository.save(user);
    }

    //-- helper --
    private Branch getBranch() {
        Faker faker = new Faker();
        long countBranch = branchRepository.count();
        return branchRepository.findById(faker.number().numberBetween(1, countBranch)).orElse(null);
    }
}
