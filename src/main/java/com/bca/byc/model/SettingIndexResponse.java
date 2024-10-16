package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingIndexResponse {

    private String id;
    private Long index;
    private String name;
    private String identity;
    private String description;
    private Integer value;
    private Boolean status;

}
