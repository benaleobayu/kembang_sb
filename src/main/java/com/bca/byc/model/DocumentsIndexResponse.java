package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentsIndexResponse extends AdminModelBaseDTOResponse<Long> {

    private String name;

    private String urlFile;

}
