package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AbstractBaseEntityCms;
import com.bca.byc.entity.AppUser;
import com.bca.byc.model.AdminModelBaseDTOResponse;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.util.helper.Formatter;

import java.util.Objects;

public class GlobalConverter {

    public <T extends AdminModelBaseDTOResponse, D extends AbstractBaseEntityCms> void CmsIDTimeStampResponse(
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

    public static String getAvatarUser (
            String avatar,
            String baseUrl
    ) {
        return Objects.isNull(avatar) || avatar.isBlank() ? null :
                avatar.startsWith("uploads/") ?
                        baseUrl + "/" + avatar :
                        avatar.startsWith("/uploads/") ? baseUrl + avatar : avatar;
    }


}
