package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class AccountDetailResponse {

    private String name;

    private Boolean status;

    private List<ChannelChecklistResponse> channels;

    private String avatar;

    private String cover;
}
