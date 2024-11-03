package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.UserInfoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

    AppUser findByEmail(String email);

    AppUser findByUsername(String subject);

    UserInfoResponse getUserDetails(String userId);
}
