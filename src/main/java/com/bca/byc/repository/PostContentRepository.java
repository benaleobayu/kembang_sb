package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostContentRepository extends JpaRepository<PostContent, Long> {

    @Modifying
    @Query("DELETE FROM PostContent p WHERE p.post = :post")
    void deletePostContentByPost(@Param("post") Post data);
}
