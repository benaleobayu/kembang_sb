package com.bca.byc.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuggestedUserProjection {

    private Long id;

    private String userId;

    private String userName;

    private String userAvatar;


}
