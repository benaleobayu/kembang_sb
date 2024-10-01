package com.bca.byc.service;

import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppUserProfileService {
    void updateUserAvatar(String email, MultipartFile avatar) throws IOException, InvalidFileTypeException;

    void updateUserCover(String email, MultipartFile cover) throws IOException, InvalidFileTypeException;

    ResultPageResponseDTO<ProfileActivityPostResponse> listDataProfileSavedActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<ProfileActivityPostResponse> listDataProfileLikesActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
