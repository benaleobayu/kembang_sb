package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserNotification;
import com.bca.byc.entity.AppUserRequestContact;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.*;
import com.bca.byc.model.apps.ProfilePostResponse;
import com.bca.byc.model.data.UserProfileActivityCounts;
import com.bca.byc.model.data.UserProfileActivityCountsProjection;
import com.bca.byc.model.projection.IdEmailProjection;
import com.bca.byc.repository.AppUserNotificationRepository;
import com.bca.byc.repository.AppUserRequestContactRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.AppUserRequestContactResponse;
import com.bca.byc.response.NotificationSettingsRequest;
import com.bca.byc.response.NotificationSettingsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserService;
import com.bca.byc.util.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final AppUserRepository appUserRepository;
    private final AppUserDTOConverter converter;
    private final PostDTOConverter postConverter;

    private final PostRepository postRepository;

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private AppUserNotificationRepository appUserNotificationRepository;

    @Autowired
    private AppUserRequestContactRepository appUserRequestContactRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("invalid user email"));
    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("invalid user email"));
    }

    @Override

    public AppUser findBySecureId(String secureId) {
        return appUserRepository.findBySecureId(secureId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with secure_id: " + secureId));
    }

    /**
     * Find users by a list of secure IDs.
     *
     * @param secureIds List of secure IDs to search for.
     * @return List of found users.
     */
    public List<AppUser> findUsersBySecureIds(List<String> secureIds) {
        // Use repository to find users by secure ID list
        return appUserRepository.findBySecureIdIn(secureIds);
    }


    @Override
    public AppUser findByUsername(String subject) {
        return appUserRepository.findByEmail(subject)
                .orElseThrow(() -> new ResourceNotFoundException("invalid user email..."));
    }

    @Override
    public UserInfoResponse getUserDetails(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"));

        AppUser data = appUserRepository.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return converter.convertToInfoResponse(data);
    }

    @Override
    public void updateUserData(String email, AppUserProfileRequest dto) {

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        converter.convertToUpdateProfile(user, dto);
        appUserRepository.save(user);
    }

    @Override
    public ProfilePostResponse getUserPosts(String userId) {
        AppUser user = appUserRepository.findBySecureId(userId)
                .orElseThrow(() -> new BadRequestException("User not found in secureId: " + userId));


        return postConverter.convertToProfilePostResponse(user);
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataMyPost(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        String email = ContextPrincipal.getPrincipal();
        IdEmailProjection user = appUserRepository.findByIdInEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Post> pageResult = postRepository.findMyPost(user.getId(), keyword, pageable);
        assert pageResult != null;
        List<PostHomeResponse> dtos = pageResult.stream().map((post) -> {
            PostHomeResponse dto = postConverter.convertToListResponse(post, user.getId());
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    public void changePassword(String userSecureId, String currentPassword, String newPassword) throws Exception {
        // Fetch user by secure ID and handle missing user using Optional
        AppUser user = appUserRepository.findBySecureId(userSecureId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if the current password matches the user's existing password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }

        // Encrypt and set the new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Save the updated user
        appUserRepository.save(user);
    }

    @Override
    public void saveNotificationSettings(String userSecureId, NotificationSettingsRequest dto) {
        // Ambil userId berdasarkan userSecureId
        Long userId = this.findBySecureId(userSecureId).getId();

        // Cek apakah pengaturan notifikasi untuk userId sudah ada
        Optional<AppUserNotification> existingNotification = appUserNotificationRepository.findByAppUserId(userId);

        // Buat atau update pengaturan notifikasi
        AppUserNotification notification = existingNotification.orElseGet(() -> {
            AppUserNotification newNotification = new AppUserNotification();
            newNotification.setAppUserId(userId);
            return newNotification;
        });
        // Set pengaturan notifikasi dari DTO
        notification.setMessages(dto.isMessages());
        notification.setFollowingFollowers(dto.isFollowingFollowers());
        notification.setPosts(dto.isPosts());
        notification.setEvents(dto.isEvents());
        notification.setLevelLoyaltyProgram(dto.isLevelLoyaltyProgram());
        notification.setNetwork(dto.isNetwork());
        notification.setPromotions(dto.isPromotions());
        notification.setNews(dto.isNews());

        // Simpan pengaturan notifikasi
        appUserNotificationRepository.save(notification);
    }

    @Override
    public NotificationSettingsResponse getNotificationSettings(String userSecureId) {
        // Fetch user by secure ID
        AppUser user = appUserRepository.findBySecureId(userSecureId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch notification by appUserId from the repository
        AppUserNotification notification = appUserNotificationRepository.findByAppUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification settings not found"));

        // Return the response DTO with notification settings
        return new NotificationSettingsResponse(notification);
    }

    @Override
    public AppUserRequestContactResponse createRequestContact(String userSecureId, String messageString) {
        // Fetch user by secure ID
        AppUser user = appUserRepository.findBySecureId(userSecureId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create a new AppUserRequestContact object and set its fields
        AppUserRequestContact requestContact = new AppUserRequestContact();
        requestContact.setAppUserId(user.getId());
        requestContact.setMessages(messageString);

        // Simpan request contact ke database melalui repository
        AppUserRequestContact savedContact = appUserRequestContactRepository.save(requestContact);

        // Buat response dari entity yang baru disimpan
        return new AppUserRequestContactResponse(savedContact.getId(), savedContact.getMessages());
    }

    @Override
    public ProfileActivityCounts getActivityCounts() {
        String uuid = ContextPrincipal.getSecureUserId();

        UserActivityCounts counts = appUserRepository.getActivityCounts(uuid);

        ProfileActivityCounts dto = new ProfileActivityCounts();
        dto.setTotalPosts(counts.getTotalPosts() != null ? counts.getTotalPosts() : 0);
        dto.setTotalFollowing(counts.getTotalFollowing() != null ? counts.getTotalFollowing() : 0);
        dto.setTotalFollowers(counts.getTotalFollowers() != null ? counts.getTotalFollowers() : 0);
        dto.setTotalEvents(0); // TODO: get total events
        return dto;
    }

    @Override
    public UserProfileActivityCounts getProfileActivityCounts() {
        String uuid = ContextPrincipal.getSecureUserId();

        UserProfileActivityCountsProjection counts = appUserRepository.getProfileActivityCounts(uuid);

        UserProfileActivityCounts dto = new UserProfileActivityCounts();
        dto.setTotalBusinesses(counts.getTotalBusinesses() != null ? counts.getTotalBusinesses() : 0);
        dto.setTotalBusinessCatalogs(counts.getTotalBusinessCatalogs() != null ? counts.getTotalBusinessCatalogs() : 0);
        dto.setTotalSavedPosts(counts.getTotalSavedPosts() != null ? counts.getTotalSavedPosts() : 0);
        dto.setTotalLikesPosts(counts.getTotalLikesPosts() != null ? counts.getTotalLikesPosts() : 0);
        dto.setTotalComments(counts.getTotalComments() != null ? counts.getTotalComments() : 0);
        return dto;
    }


}
