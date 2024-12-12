package com.kembang.service;

import com.kembang.entity.AppAdmin;
import com.kembang.model.UserDetailResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppAdminService extends UserDetailsService {

    UserDetailResponseDTO findUserDetail();

    AppAdmin findByEmail(String email);
}
