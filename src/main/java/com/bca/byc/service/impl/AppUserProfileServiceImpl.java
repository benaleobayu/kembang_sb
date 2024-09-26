package com.bca.byc.service.impl;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.util.FileUploadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AppUserProfileServiceImpl implements AppUserProfileService {

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;

    @Override
    public void updateUserAvatar(String email, MultipartFile avatar) throws IOException, InvalidFileTypeException {
        FileUploadHelper.validateFileTypeImage(avatar);

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldAvatar = userDetail.getAvatar();
        String avatarPath = FileUploadHelper.saveFile(avatar, UPLOAD_DIR + "/avatar");

        userDetail.setAvatar(avatarPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        userRepository.save(appUser);

        if (oldAvatar != null && !oldAvatar.isEmpty()) {
            FileUploadHelper.deleteFile(oldAvatar);
        }
    }

    @Override
    public void updateUserCover(String email, MultipartFile cover) throws IOException, InvalidFileTypeException {
        FileUploadHelper.validateFileTypeImage(cover);

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldCover = userDetail.getCover();
        String coverPath = FileUploadHelper.saveFile(cover, UPLOAD_DIR + "/cover");

        userDetail.setCover(coverPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        userRepository.save(appUser);

        if (oldCover != null && !oldCover.isEmpty()) {
            FileUploadHelper.deleteFile(oldCover);
        }
    }
}
