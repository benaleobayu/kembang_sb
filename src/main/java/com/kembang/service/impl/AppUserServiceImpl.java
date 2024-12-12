package com.kembang.service.impl;

import com.kembang.entity.AppUser;
import com.kembang.exception.ResourceNotFoundException;
import com.kembang.model.UserInfoResponse;
import com.kembang.repository.AppUserRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final AppUserRepository appUserRepository;

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
    public UserInfoResponse getUserDetails(String userId) {
        return null;
    }


    // --- Helper ---
    private AppUser getUserBySecureId(String userId) {
        return HandlerRepository.getIdBySecureId(
                userId,
                appUserRepository::findBySecureId,
                projection -> appUserRepository.findById(projection.getId()),
                "User not found"
        );
    }
}
