package com.bca.byc.service;

import com.bca.byc.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCmsService {


    Page<User> getRolePagination(Pageable pageable);

    void createUser(User user);

}
