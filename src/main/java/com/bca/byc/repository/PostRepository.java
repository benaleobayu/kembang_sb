package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByDescription(String title);

    Page<Post> findByDescriptionLikeIgnoreCase(String title, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
            "(SELECT u.appUserAttribute.id FROM AppUser u WHERE u.appUserAttribute.id IN " +
            "(SELECT ua.id FROM AppUserAttribute ua WHERE ua.isRecommended = true))" +
            "ORDER BY function('RANDOM') ")
    Page<Post> findRandomPosts(String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
            "(SELECT u.id FROM AppUser u JOIN u.follows f WHERE :userId = f.id)" +
            "AND LOWER (p.description) LIKE LOWER(:tag) ")
    Page<Post> findLatestPostsFromFollowingUsers(@Param("userId") Long userId, @Param("tag") String tag, Pageable pageable);
}
