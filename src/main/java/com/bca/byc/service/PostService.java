package com.bca.byc.service;

import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.List;

public interface PostService {

    PostDetailResponse findById(Long id) throws Exception;

    void save(String email, PostCreateUpdateRequest dto, List<PostContent> contentList)  throws Exception, InvalidFileTypeException;

    void update(Long id, PostCreateUpdateRequest post)  throws Exception;

    void deleteData(Long id)  throws Exception;

    ResultPageResponseDTO<PostHomeResponse> listData(String email,Integer pages, Integer limit, String sortBy, String direction, String tag, String categories);

//    String uploadContent(MultipartFile fileName) throws IOException;
//
//    public byte[] downloadContent(String fileName) throws IOException;

}
