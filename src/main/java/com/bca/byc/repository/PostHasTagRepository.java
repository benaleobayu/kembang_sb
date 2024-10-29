package com.bca.byc.repository;

import com.bca.byc.entity.PostHasTags;
import com.bca.byc.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostHasTagRepository extends JpaRepository<PostHasTags, Long> {

    @Query("SELECT pt.tag FROM PostHasTags pt WHERE pt.post.id = :id")
    Set<Tag> getTagsByPostId(@Param("id") Long id);
}