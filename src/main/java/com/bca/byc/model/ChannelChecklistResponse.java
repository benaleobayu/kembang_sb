package com.bca.byc.model;

import lombok.Data;

@Data
public class ChannelChecklistResponse {

    private String id;
    private String name;
    private Boolean isChecked;

}
