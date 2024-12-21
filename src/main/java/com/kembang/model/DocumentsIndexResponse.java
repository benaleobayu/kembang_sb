package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentsIndexResponse extends ModelBaseDTOResponse<Long> {

    private String name;

    private String urlFile;

}
