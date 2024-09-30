package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.model.apps.ProfilePostResponse;
import com.bca.byc.response.NotificationSettingsRequest;
import com.bca.byc.response.NotificationSettingsResponse;

import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bca.byc.model.UserDetailResponseDTO;

public interface AppUserService extends UserDetailsService {

	AppUser findByEmail(String email);

	AppUser findByUsername(String subject);
	AppUser findBySecureId(String secureId);

	UserInfoResponse getUserDetails(String name);

	void updateUserData(String email, AppUserProfileRequest dto);

    ProfilePostResponse getUserPosts(String userId);

	void changePassword(String userSecureId, String currentPassword, String newPassword) throws Exception;
	void saveNotificationSettings(String userSecureId, NotificationSettingsRequest dto);
	NotificationSettingsResponse getNotificationSettings(String userSecureId);
	
	
}
