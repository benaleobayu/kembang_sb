package com.bca.byc.model.apps;

import lombok.Data;

@Data
public class TagUserResponse {

    private Long id;
    private String name;
    private String avatar;

    public TagUserResponse() {
    }

    public TagUserResponse(Long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
}
