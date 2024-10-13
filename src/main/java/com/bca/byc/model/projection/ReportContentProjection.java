package com.bca.byc.model.projection;

import com.bca.byc.entity.*;

import java.time.LocalDateTime;

public interface ReportContentProjection {

    Report getReport();

    Post getPost();

    PostContent getPostContent();

    AppUser getUser();

    AppUserDetail getUserDetail();

    AppUserAttribute getAppUserAttribute();

    Tag getTag();

    Channel getChannel();
}
