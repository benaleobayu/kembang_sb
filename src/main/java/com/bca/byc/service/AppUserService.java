package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.ApiUserInfoDetailResponse;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.model.UserSetPasswordRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bca.byc.model.UserDetailResponseDTO;

public interface AppUserService extends UserDetailsService {

	AppUser findByEmail(String email);

	public UserDetailResponseDTO findUserDetail();

	void createNewUser(@Valid LoginRequestDTO dto);

	AppUser findByUsername(String subject);

	ApiUserInfoDetailResponse getUserDetails(String name);

	void followUser(Long userId, String name);

	void unfollowUser(Long userId, String name);
}
