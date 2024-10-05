package com.bca.byc.service;

import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.List;

public interface PostService {

//    PostDetailResponse findById(Long id) throws Exception;

    ResultPageResponseDTO<PostHomeResponse> listDataPostHome(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword, String category);

    PostDetailResponse findBySecureId(String secureId);

    void save(String email, PostCreateUpdateRequest dto, List<PostContent> contentList) throws Exception, InvalidFileTypeException;

    void update(String secureId, PostCreateUpdateRequest post) throws Exception;

    void deleteData(String secureId) throws Exception;

//    String uploadContent(MultipartFile fileName) throws IOException;
//
//    public byte[] downloadContent(String fileName) throws IOException;

}
