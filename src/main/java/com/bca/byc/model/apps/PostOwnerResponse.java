package com.bca.byc.model.apps;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostOwnerResponse {

    private String id;
    private String avatar;
    private String name;
    private Boolean isMyAccount = false;
    private Boolean isFollowed = false;

    private String businessName;
    private String lineOfBusiness;
    private Boolean isPrimary;

    public PostOwnerResponse() {

    }

}
