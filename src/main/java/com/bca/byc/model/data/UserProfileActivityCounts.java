package com.bca.byc.model.data;

import lombok.Data;

@Data
public class UserProfileActivityCounts {
    private Integer totalBusinesses;
    private Integer totalBusinessCatalogs;
    private Integer totalSavedPosts;
    private Integer totalLikesPosts;
    private Integer totalComments;
}
