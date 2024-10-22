package com.bca.byc.service;

import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppUserProfileService {
    UserInfoResponse updateUserAvatar(MultipartFile avatar) throws IOException, InvalidFileTypeImageException;

    UserInfoResponse updateUserCover(MultipartFile cover) throws IOException, InvalidFileTypeImageException;

    ResultPageResponseDTO<PostHomeResponse> listDataProfileSavedActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<PostHomeResponse> listDataProfileLikesActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<ProfileActivityPostCommentsResponse> listDataPostCommentsActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
