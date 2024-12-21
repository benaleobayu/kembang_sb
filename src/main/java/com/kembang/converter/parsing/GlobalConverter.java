package com.kembang.converter.parsing;

import com.kembang.entity.AbstractBaseEntity;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.AppUser;
import com.kembang.model.ModelBaseDTOResponse;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.AppUserRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.util.PaginationUtil;
import com.kembang.util.helper.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class GlobalConverter {

    public static <D extends AbstractBaseEntity> void CmsAdminCreateAtBy(
            D data, Long admin
    ) {
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
    }

    public static <D extends AbstractBaseEntity> void CmsAdminUpdateAtBy(
            D data, Long admin
    ) {
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }


    public static <T extends ModelBaseDTOResponse<Long>, D extends AbstractBaseEntity> void CmsIDTimeStampResponseAndId(
            T dto, D data, AppAdminRepository adminRepository
    ) {
        dto.setIndex(data.getId());
        parseCmsIDTimeStampResponse(dto, data, adminRepository);
    }
    public static <T extends ModelBaseDTOResponse<Integer>, D extends AbstractBaseEntity> void CmsIDTimeStampResponseAndIdAtomic(
            T dto, D data, AppAdminRepository adminRepository, AtomicInteger index
    ) {
        dto.setIndex(index.getAndIncrement());
        parseCmsIDTimeStampResponse(dto, data, adminRepository);
    }

    private static <T extends ModelBaseDTOResponse<?>, D extends AbstractBaseEntity> void parseCmsIDTimeStampResponse(
            T dto, D data, AppAdminRepository adminRepository
    ) {
        dto.setId(data.getSecureId());
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        dto.setUpdatedAt(data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null);
        AppAdmin createdBy = data.getCreatedBy() == null ? null : HandlerRepository.getEntityById(data.getCreatedBy(), adminRepository, "Admin not found");
        AppAdmin updatedBy = data.getUpdatedBy() == null ? null : HandlerRepository.getEntityById(data.getUpdatedBy(), adminRepository, "Admin not found");

        dto.setCreatedBy(data.getCreatedBy() != null ? Objects.requireNonNull(createdBy).getName() : null);
        dto.setUpdatedBy(data.getUpdatedBy() != null ? Objects.requireNonNull(updatedBy).getName() : null);
    }


    public static <T extends ModelBaseDTOResponse<Integer>, D extends AbstractBaseEntity> void SetCmsTimeStampResponse(
            T dto, D data, AppAdminRepository adminRepository
    ) {
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        dto.setUpdatedAt(data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null);
        AppAdmin createdBy = HandlerRepository.getEntityById(data.getCreatedBy(), adminRepository, "Admin not found");
        AppAdmin updatedBy = HandlerRepository.getEntityById(data.getUpdatedBy(), adminRepository, "Admin not found");

        dto.setCreatedBy(data.getCreatedBy() != null ? createdBy.getName() : null);
        dto.setUpdatedBy(data.getUpdatedBy() != null ? updatedBy.getName() : null);
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

    // Helper get start date
    public static LocalDate getDefaultLocalDate(LocalDate date, String type){
        LocalDate startDate = (date != null) ? date : LocalDate.of(1970, 1, 1);
        LocalDate endDate = (date != null) ? date : LocalDate.now().plusYears(1);

        return type.equals("start") ? startDate : endDate;
    }



    // Helper pagination
    public static SavedKeywordAndPageable appsCreatePageable(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String keyword,
            ListOfFilterPagination discardList
    ) {

        // Add wildcard to keyword for SQL LIKE queries
        keyword = StringUtils.isEmpty(keyword) ? "%" : "%" + keyword + "%";

        // Create pageable with sort direction and sorting field
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        return new SavedKeywordAndPageable(keyword, pageable);
    }

    // Helper pagination
    public static SavedKeywordAndPageable createPageable(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String keyword
    ) {

        keyword = StringUtils.isEmpty(keyword) ? "%" : "%" + keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        return new SavedKeywordAndPageable(keyword, pageable);
    }

    public static Pageable oldSetPageable(Integer pages, Integer limit, String sortBy, String direction, Page<?> firstResult, Long totalData) {
        long totalRecords = firstResult != null ? firstResult.getTotalElements() : totalData;
        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        if (pages >= totalPages) {
            pages = 0;
        }
        return PageRequest.of(pages, limit, Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy)));
    }



    public static SavedKeywordAndPageable eZcreatePageable(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String keyword,
            Page<?> firstResult,
            Long totalData) {

        long totalRecords = firstResult != null ? firstResult.getTotalElements() : totalData;
        log.info("Total records: " + totalRecords);
        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        log.info("Total pages: " + totalPages);
        if (pages >= totalPages) {
            pages = 0;
            log.info("Reset pages to 0");
        }

        keyword = StringUtils.isEmpty(keyword) ? "%" : "%" + keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        return new SavedKeywordAndPageable(keyword, pageable);
    }



}
