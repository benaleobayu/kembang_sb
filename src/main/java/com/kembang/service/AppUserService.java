package com.kembang.service;

import com.kembang.entity.AppUser;
import com.kembang.model.UserInfoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

    AppUser findByEmail(String email);

    AppUser findByUsername(String subject);

    UserInfoResponse getUserDetails(String userId);
}
