package com.bca.byc.model.projection;

import com.bca.byc.entity.*;

public interface ReportDataProjection {

    Report getReport();

    Post getPost();

    PostContent getPostContent();

    AppUser getUser();

    AppUserDetail getUserDetail();

    AppUserAttribute getAppUserAttribute();

    Tag getTag();

    Channel getChannel();
}
