package com.bca.byc.model.apps;

import com.bca.byc.converter.UserManagementConverter;
import com.bca.byc.entity.AppUser;
import lombok.Data;

@Data
public class OwnerDataResponse {

    private String id;
    private String name;
    private String avatar;

}
