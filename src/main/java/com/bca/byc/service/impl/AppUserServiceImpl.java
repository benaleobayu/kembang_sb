package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.apps.ProfilePostResponse;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserDTOConverter converter;
    private final PostDTOConverter postConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
    
}
