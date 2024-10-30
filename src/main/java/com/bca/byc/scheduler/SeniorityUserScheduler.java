package com.bca.byc.scheduler;

import com.bca.byc.repository.AppUserDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class SeniorityUserScheduler {

    private final AppUserDetailRepository appUserDetailRepository;

    @Scheduled(cron = "0 0 0 * * *") // every day on 00:00
    public void updateSeniorityStatus() {
        LocalDate now = LocalDate.now();
        appUserDetailRepository.findAll().forEach(data -> {
            LocalDate birthdate = data.getMemberBirthdate();
            if (birthdate != null && now.minusYears(35).isAfter(birthdate)) {
                data.setIsSenior(true);
            } else {
                data.setIsSenior(false);
            }
            appUserDetailRepository.save(data);
        });
    }
}