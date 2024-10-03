package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PostCategory;
import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;
import com.bca.byc.repository.auth.AppAdminRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PostCategoryDTOConverter {

    private final AppAdminRepository adminRepository;
    private ModelMapper modelMapper;

    // for get data
    public PostCategoryDetailResponse convertToListResponse(PostCategory data) {
        // mapping Entity with DTO Entity
        GlobalConverter converter = new GlobalConverter();
        PostCategoryDetailResponse dto = new PostCategoryDetailResponse();
        dto.setName(data.getName());
        dto.setIcon(data.getIcon());
        // format
        converter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

    // for create data
    public PostCategory convertToCreateRequest(@Valid PostCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        PostCategory data = modelMapper.map(dto, PostCategory.class);
        // Get email from security context


        // createdBy
        data.setCreatedBy(getAdmin());
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PostCategory data, @Valid PostCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_by
        data.setUpdatedBy(getAdmin());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

    private AppAdmin getAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof AppAdmin) {
            email = ((AppAdmin) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = null;
        }
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }
}
