package com.bca.byc.model.projection;

import com.bca.byc.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportUserIndexProjection {

    private String id;

    private Long index;

    private Long reportedId;

    private String reportedEmail;

    private AppUser reportedUser;

    private String reporterEmail;

}
