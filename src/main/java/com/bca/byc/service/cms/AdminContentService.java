package com.bca.byc.service.cms;

import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminContentDetailResponse;
import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.List;

public interface AdminContentService {

    ResultPageResponseDTO<AdminContentIndexResponse<Long>> listDataAdminContentIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    AdminContentIndexResponse<Long> getDataTeaser();

    AdminContentDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(List<PostContent> contentList, Post newPost);

    void deleteData(String id) throws BadRequestException;

    void updateData(Post updatePost);

    void updateTeaserStatus(String postId);
}

