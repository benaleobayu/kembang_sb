package com.bca.byc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class AppUserDetailNameQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2151151741748474211L;

    private Long id;

    private String name;

}
