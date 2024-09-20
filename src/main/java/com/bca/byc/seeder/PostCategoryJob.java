package com.bca.byc.seeder;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PostCategory;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.repository.PostCategoryRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class PostCategoryJob {

    private final PostCategoryRepository postCategoryRepository;
    private final AppAdminRepository adminRepository;

//    @Scheduled(fixedDelay = 50)
    public void run() {
        Faker faker = new Faker();

        AppAdmin createAdmin = adminRepository.findByEmail("admin-opt@unictive.net")
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        PostCategory data = new PostCategory(
                null,
                faker.avatar().image(),
                faker.company().industry(),
                faker.lorem().characters(60),
                createAdmin,
                null
        );

        postCategoryRepository.save(data);

    }

}
