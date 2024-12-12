package com.kembang.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminPermissionResponse {

    private List<String> permissions;

}