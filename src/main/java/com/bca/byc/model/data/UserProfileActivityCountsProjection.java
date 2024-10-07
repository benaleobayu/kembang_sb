package com.bca.byc.model.data;

public interface UserProfileActivityCountsProjection {
    Integer getTotalBusinesses();

    Integer getTotalBusinessCatalogs();

    Integer getTotalSavedPosts();

    Integer getTotalLikesPosts();

    Integer getTotalComments();
}

