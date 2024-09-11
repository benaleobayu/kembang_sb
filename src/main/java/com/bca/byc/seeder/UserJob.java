package com.bca.byc.seeder;

import com.bca.byc.entity.AppUser;
import com.bca.byc.repository.auth.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJob {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Scheduled(fixedDelay = 50)
    public void saveDataInDb() {
        Faker faker = new Faker();
        AppUser user = new AppUser(null,
                faker.name().username(),
                faker.internet().emailAddress(),
                passwordEncoder.encode("password"));
        appUserRepository.save(user);
    }
}
