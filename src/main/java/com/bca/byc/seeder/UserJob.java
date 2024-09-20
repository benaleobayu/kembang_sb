package com.bca.byc.seeder;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.AppUserAttributeRepository;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserJob {

    private final AppUserRepository appUserRepository;
    private final AppUserDetailRepository appUserDetailRepository;
    private final AppUserAttributeRepository appUserAttributeRepository;

    private final PasswordEncoder passwordEncoder;

//    @Scheduled(fixedDelay = 50)
    public void saveDataInDb() {
        Faker faker = new Faker();
        String[] memberType = {"Solitaire", "Priority"};
        UserType[] userType = {UserType.MEMBER, UserType.NOT_MEMBER};
        StatusType[] statusTypes = {StatusType.REJECTED, StatusType.PRE_ACTIVATED, StatusType.ACTIVATED};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);
        String [] memberAs = {"member", "child"};
        AppUserDetail userDetail = new AppUserDetail(
                null,
                faker.name().fullName(),
                faker.phoneNumber().phoneNumber(),
                cardNumber.toString(),
                cardNumber.toString(),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35)),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35)),
                cinNumber.toString(),
                cinNumber.toString(),
                faker.educator().campus(),
                faker.lorem().characters(120),
                statusTypes[faker.number().numberBetween(0,3)],
                userType[faker.number().numberBetween(0,2)],
                memberType[faker.number().numberBetween(0,2)],
                "SYSTEM",
                LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)),
                faker.avatar().image(),
                faker.avatar().image(),
                memberAs[faker.number().numberBetween(0,2)]
        );
        AppUserDetail saveUserDetail = appUserDetailRepository.save(userDetail);

        AppUserAttribute appUserAttribute = new AppUserAttribute(
                null,
                faker.random().nextBoolean(),
                faker.random().nextBoolean(),
                faker.random().nextBoolean(),
                faker.random().nextBoolean(),
                faker.random().nextBoolean(),
                faker.random().nextBoolean()
        );
        AppUserAttribute saveUserAttribute = appUserAttributeRepository.save(appUserAttribute);

        AppUser user = new AppUser(null,
                faker.name().username(),
                faker.internet().emailAddress(),
                passwordEncoder.encode("password")
        );
        appUserRepository.save(user);
    }
}
