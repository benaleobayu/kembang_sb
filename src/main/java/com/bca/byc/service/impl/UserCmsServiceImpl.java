package com.bca.byc.service.impl;

import com.bca.byc.entity.User;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.UserCmsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCmsServiceImpl implements UserCmsService {

    private UserRepository repository;

    @Override
    public Page<User> getRolePagination(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void createUser(User user) {
        repository.save(user);
    }
}
