package com.kembang.service.impl;

import com.kembang.entity.AppAdmin;
import com.kembang.exception.ResourceNotFoundException;
import com.kembang.model.UserDetailResponseDTO;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.service.AppAdminService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppAdminServiceImpl implements AppAdminService {

    private AppAdminRepository appAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appAdminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("invalid email admin"));
    }

    @Override
    public UserDetailResponseDTO findUserDetail() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        UserDetailResponseDTO dto = new UserDetailResponseDTO();
        String username = ctx.getAuthentication().getName();
        dto.setUsername(username);
        return dto;
    }

    @Override
    public AppAdmin findByEmail(String email) {
        return appAdminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("invalid email admin"));
    }

}
