package com.bca.byc.seeder;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.PreRegisterRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PreRegisterJob {

    private final PreRegisterRepository repository;

    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        String[] memberType = {"SOLITAIRE", "PRIORITY", "NOT_MEMBER"};
        UserType[] userType = {UserType.MEMBER, UserType.NOT_MEMBER};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);

        PreRegister data = new PreRegister(
                null,
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber().toString(),
                memberType[faker.number().numberBetween(0, 3)],
                userType[faker.number().numberBetween(0, 2)],
                faker.lorem().characters(120),
                cardNumber.toString(),
                cinNumber.toString(),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)),
                cardNumber.toString(),
                cinNumber.toString(),
                LocalDate.now().minusDays(faker.number().numberBetween(365 * 10, 365 * 45)),
                faker.nation().language().toUpperCase(),
                faker.name().firstName(),
                1,
                true
        );

        repository.save(data);

    }

}
