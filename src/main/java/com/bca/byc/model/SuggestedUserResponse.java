package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedUserResponse {

    private String userId;
    private String userName;
    private String userAvatar;

    private String businessName;
    private String businessLob;
}
