package com.bca.byc.service;

import com.bca.byc.exception.InvalidFileTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppUserProfileService {
    void updateUserAvatar(String email, MultipartFile avatar) throws IOException, InvalidFileTypeException;

    void updateUserCover(String email, MultipartFile cover) throws IOException, InvalidFileTypeException;
}
