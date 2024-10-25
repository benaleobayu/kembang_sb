package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.AdminContentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminContentDetailResponse;
import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AdminContentRepository;
import com.bca.byc.repository.PostContentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.AdminContentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminContentServiceImpl implements AdminContentService {

    private final AppAdminRepository adminRepository;
    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    @Value("${app.base.url}")
    private String baseUrl;
    private AdminContentRepository repository;
    private AdminContentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AdminContentIndexResponse<Long>> listDataAdminContentIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Post> pageResult = repository.findDataPostByAdmin(set.keyword(), set.pageable());
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

        TreePostConverter treePostConverter = new TreePostConverter(null, null);
        String contentType = treePostConverter.getContentTypePost(contentList);

        newPost.setContentType(contentType);
        GlobalConverter.CmsAdminCreateAtBy(newPost, admin);
        Post savedPost = postRepository.save(newPost);

        for (PostContent postContent : contentList) {
            postContent.setPost(savedPost);
            postContentRepository.save(postContent);
        }
    }

    @Override
    public void updateData(Post updatePost) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);

        // update the updated_at
        GlobalConverter.CmsAdminUpdateAtBy(updatePost, admin);
        // save
        repository.save(updatePost);
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
