package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.AdminContentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.bca.byc.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class AdminContentServiceImpl implements AdminContentService {

    @Value("${app.base.url}")
    private String baseUrl;

    private final AppAdminRepository adminRepository;
    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;

    private AdminContentRepository repository;
    private AdminContentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AdminContentIndexResponse<Long>> listDataAdminContentIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Post> pageResult = repository.findDataPostByAdmin(keyword, pageable);
        List<AdminContentIndexResponse<Long>> dtos = pageResult.stream().map((c) -> {
            AdminContentIndexResponse<Long> dto = converter.convertToIndexResponse(c, baseUrl);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public AdminContentDetailResponse findDataById(String id) throws BadRequestException {
        Post data = HandlerRepository.getEntityBySecureId(id, repository, "Admin Content not found");

        return converter.convertToDetailResponse(data, baseUrl);
    }

    @Override
    public void saveData(List<PostContent> contentList, Post newPost) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);

        newPost.setAdmin(admin);
        newPost.setIsAdminPost(true);

        Post savedPost = postRepository.save(newPost);

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
