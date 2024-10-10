package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AbstractBaseEntityCms;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.model.AdminModelBaseDTOResponse;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.util.helper.Formatter;

import java.time.LocalDateTime;
import java.util.Objects;

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


    public static <T extends AdminModelBaseDTOResponse<Long>, D extends AbstractBaseEntityCms> void CmsIDTimeStampResponse(
            T dto, D data
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
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

    public static String replaceImagePath(String imagePath) {
        return imagePath.replace("src/main/resources/static/", "/");
    }


}
