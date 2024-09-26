package com.bca.byc.service.impl;

import com.bca.byc.converter.AppUserDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        userConverter.convertToUpdateProfile(user, dto);
        appUserRepository.save(user);
    }

}
