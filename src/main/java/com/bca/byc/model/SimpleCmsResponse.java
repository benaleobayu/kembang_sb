package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCmsResponse extends ModelBaseDTOResponse<Long> {

    private String name;

    private Boolean isActive;

}
