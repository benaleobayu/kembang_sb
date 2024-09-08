package com.bca.byc.service;

import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;

public interface PostService {

    void init();

    PostDetailResponse findById(Long id) throws Exception;

    void save(String email, PostCreateUpdateRequest dto)  throws Exception;

    void update(Long id, PostCreateUpdateRequest post)  throws Exception;

    void deleteData(Long id)  throws Exception;

//    String uploadContent(MultipartFile fileName) throws IOException;
//
//    public byte[] downloadContent(String fileName) throws IOException;

}
