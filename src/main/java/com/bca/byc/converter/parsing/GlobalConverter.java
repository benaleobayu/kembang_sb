package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AbstractBaseEntityCms;
import com.bca.byc.model.AdminModelBaseDTOResponse;
import com.bca.byc.util.helper.Formatter;

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


}
