package com.bca.byc.service;

import com.bca.byc.model.UserDetailResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppAdminService extends UserDetailsService {

    public UserDetailResponseDTO findUserDetail();
}
