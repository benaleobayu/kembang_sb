package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.*;
import com.bca.byc.model.apps.ProfilePostResponse;
import com.bca.byc.model.data.UserProfileActivityCounts;
import com.bca.byc.response.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

	AppUser findByEmail(String email);

	AppUser findByUsername(String subject);
	AppUser findBySecureId(String secureId);

	UserInfoResponse getUserDetails(String userId);

	void updateUserData(String email, AppUserProfileRequest dto);

	ProfilePostResponse getUserPosts(String userId);
	ResultPageResponseDTO<PostHomeResponse> listDataMyPost(Integer pages, Integer limit, String sortBy, String direction, String keyword, String userId);
	ResultPageResponseDTO<PostHomeResponse> listDataTagPost(Integer pages, Integer limit, String sortBy, String direction, String keyword, String userId);

	void changePassword(String userSecureId, String currentPassword, String newPassword) throws Exception;
	void saveNotificationSettings(String userSecureId, NotificationSettingsRequest dto);
	NotificationSettingsResponse getNotificationSettings(String userSecureId);

	AppUserRequestContactResponse  createRequestContact(String userSecureId, String messageString);

	ProfileActivityCounts getActivityCounts(String userId);

	UserProfileActivityCounts getProfileActivityCounts(String userId);
}
