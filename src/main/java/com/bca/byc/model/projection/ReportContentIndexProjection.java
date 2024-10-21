package com.bca.byc.model.projection;

import com.bca.byc.entity.Post;
import com.bca.byc.entity.Tag;

import java.time.LocalDateTime;
import java.util.Set;

public interface ReportContentIndexProjection {

    String getId();

    Long getIndex();

    String getHighlight();

    String getThumbnail();

    String getDescription();

    Set<Tag> getTags();

    String getCreator();

    String getReporterEmail();

    Long getTotalReport();

    LocalDateTime getLastReportAt();

    String getChannelName();

    Post getPost();

    String getStatusReport();

}
