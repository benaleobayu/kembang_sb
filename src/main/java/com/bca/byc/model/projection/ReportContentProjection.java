package com.bca.byc.model.projection;

import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;

import java.time.LocalDateTime;

public interface ReportContentProjection {
    String getId();

    String getHighlight();

    String getThumbnail();

    String getTags();

    String getCreator();

    String getStatusReport();

    Long getTotalReport();

    LocalDateTime getLastReportAt();

    Post getPost();

    String getChannelName();

    String getPostDescription();
}
