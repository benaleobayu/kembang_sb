package com.bca.byc.service.impl;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.UserDetailResponseDTO;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.service.AppAdminService;
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
