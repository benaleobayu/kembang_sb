package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AbstractBaseEntityCms;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.model.AdminModelBaseDTOResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.util.PaginationUtil;
import com.bca.byc.util.helper.Formatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GlobalConverter {

    public static <D extends AbstractBaseEntityCms> void CmsAdminCreateAtBy(
            D data, AppAdmin admin
    ) {
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
    }

    public static <D extends AbstractBaseEntityCms> void CmsAdminUpdateAtBy(
            D data, AppAdmin admin
    ) {
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }


    public static <T extends AdminModelBaseDTOResponse<Long>, D extends AbstractBaseEntityCms> void CmsIDTimeStampResponseAndId(
            T dto, D data
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        dto.setUpdatedAt(data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null);
        dto.setCreatedBy(data.getCreatedBy() != null ? data.getCreatedBy().getName() : null);
        dto.setUpdatedBy(data.getUpdatedBy() != null ? data.getUpdatedBy().getName() : null);
    }

    public static <T extends AdminModelBaseDTOResponse<Integer>, D extends AbstractBaseEntityCms> void CmsTimeStampResponse(
            T dto, D data
    ) {
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        dto.setUpdatedAt(data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null);
        dto.setCreatedBy(data.getCreatedBy() != null ? data.getCreatedBy().getName() : null);
        dto.setUpdatedBy(data.getUpdatedBy() != null ? data.getUpdatedBy().getName() : null);
    }

    public static String getUuidUser(
            AppUserRepository userRepository
    ) {
        String email = ContextPrincipal.getPrincipal();
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");

        return user.getSecureId();
    }

    public static AppUser getUserEntity(
            AppUserRepository userRepository
    ) {
        String email = ContextPrincipal.getPrincipal();
        return HandlerRepository.getUserByEmail(email, userRepository, "User not found");
    }

    public static AppAdmin getAdminEntity(
            AppAdminRepository adminRepository
    ) {
        String email = ContextPrincipal.getPrincipal();
        return HandlerRepository.getAdminByEmail(email, adminRepository, "Admin not found");
    }

    public static String getParseImage(
            String imageUrl,
            String baseUrl
    ) {
        return Objects.isNull(imageUrl) || imageUrl.isBlank() ? null :
                imageUrl.startsWith("uploads/") ?
                        baseUrl + "/" + imageUrl :
                        imageUrl.startsWith("/uploads/") ? baseUrl + imageUrl : imageUrl;
    }
    public static String getAvatarImage(
            String imageUrl,
            String baseUrl
    ) {
        return Objects.isNull(imageUrl) || imageUrl.isBlank() ? baseUrl + "/uploads/images/avatar.png" :
                imageUrl.startsWith("uploads/") ?
                        baseUrl + "/" + imageUrl :
                        imageUrl.startsWith("/uploads/") ? baseUrl + imageUrl : imageUrl;
    }


    public static String replaceImagePath(String imagePath) {
        return imagePath.replace("src/main/resources/static/", "/");
    }

    // Helper highlight list to array
    public static List<String> convertListToArray(String lists) {
        List<String> data = new ArrayList<>();
        String[] listArray = lists != null ? lists.split(",") : new String[0];
        for (String list : listArray) {
            data.add(list.trim());
        }
        return data;
    }

    // Helper for parsing tag
    public static String convertTagString(String tagList) {
        String tagName = tagList.replaceAll("[^a-zA-Z0-9\\s]", "") // Remove special characters
                .trim()
                .replaceAll("\\s+", " "); // Normalize spaces

        // Capitalize the first letter of each word and remove spaces
        return Arrays.stream(tagName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    // Helper pagination
    public static SavedKeywordAndPageable createPageable(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String keyword,
            ListOfFilterPagination discardList
    ) {
        // Check if any of the fields in discardList are not null or empty
        if (discardList.getKeyword() != null && discardList.getKeyword().length() >= 3) {
            pages = 0;
        }
        if (discardList.getStartDate() != null && discardList.getStartDate().toString().length() >= 3) {
            pages = 0;
        }
        if (discardList.getEndDate() != null && discardList.getEndDate().toString().length() >= 3) {
            pages = 0;
        }
        if (discardList.getAdminApprovalStatus() != null ) {
            pages = 0;
        }

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        return new SavedKeywordAndPageable(keyword, pageable);
    }


}
