package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.AdminContentDTOConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminContentCreateUpdateRequest;
import com.bca.byc.model.AdminContentDetailResponse;
import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.repository.AdminContentRepository;
import com.bca.byc.repository.PostContentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.AdminContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminContentServiceImpl implements AdminContentService {

    private final AppAdminRepository adminRepository;
    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;

    private AdminContentRepository repository;
    private AdminContentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AdminContentIndexResponse> listDataAdminContentIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        return null;
    }

    @Override
    public AdminContentDetailResponse findDataById(String id) throws BadRequestException {
        Post data = HandlerRepository.getEntityBySecureId(id, repository, "Admin Content not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public List<AdminContentDetailResponse> findAllData() {
        // Get the list
        List<Post> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(List<PostContent> contentList, Post newPost) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);

        newPost.setAdmin(admin); // Contoh, jika Anda perlu menyimpan admin yang membuat post

        // Simpan post pertama
        Post savedPost = postRepository.save(newPost);

        // Simpan setiap content yang terkait
        for (PostContent postContent : contentList) {
            postContent.setPost(savedPost);
            postContentRepository.save(postContent);
        }
    }

    @Override
    public void updateData(String id, AdminContentCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Post data = HandlerRepository.getEntityBySecureId(id, repository, "Admin Content not found");

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());
        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Post data = HandlerRepository.getEntityBySecureId(id, repository, "Admin Content not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("AdminContent not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}
