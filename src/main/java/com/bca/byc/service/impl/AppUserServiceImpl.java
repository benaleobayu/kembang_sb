package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.model.UserDetailResponseDTO;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserDTOConverter converter;

    private final AppUserDTOConverter userConverter;

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
    public UserDetailResponseDTO findUserDetail() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        UserDetailResponseDTO dto = new UserDetailResponseDTO();
        String username = ctx.getAuthentication().getName();
        dto.setUsername(username);
        return dto;
    }

    @Override
    public void createNewUser(LoginRequestDTO dto) {

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
    public void followUser(Long userId, String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToFollow = appUserRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            appUserRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(Long userId, String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToUnfollow = appUserRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            appUserRepository.save(user);
        }
    }

    @Override
    public void updateUserData(String email, AppUserProfileRequest dto) {

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        userConverter.convertToUpdateProfile(user, dto);
        appUserRepository.save(user);
    }

}
