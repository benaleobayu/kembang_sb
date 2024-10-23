package com.bca.byc.seeder;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserRequestContact;
import com.bca.byc.enums.RequestType;
import com.bca.byc.repository.AppUserRequestContactRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContactRequestJob {


    private final AppUserRepository userRepository;
    private final AppUserRequestContactRepository contactRepository;

    //    @Scheduled(fixedDelay = 50)
    public void saveDataInDb() {
        Faker faker = new Faker();
        RequestType[] requestTypes = {RequestType.PENDING, RequestType.IN_PROGRESS, RequestType.COMPLETED, RequestType.CANCELLED};
        Long totalUser = userRepository.count();
        Integer randomUser = faker.random().nextInt(1, totalUser.intValue());
        AppUser user = userRepository.findById(Long.valueOf(randomUser)).orElse(null);


        AppUserRequestContact request = new AppUserRequestContact();
        request.setId(null);
        request.setUser(user);
        request.setMessages(faker.lorem().sentence(10));
        request.setStatus(requestTypes[faker.random().nextInt(0, 5)]);
        contactRepository.save(request);

    }

}
