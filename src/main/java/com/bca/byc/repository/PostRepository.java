package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import com.bca.byc.model.projection.IdSecureIdProjection;
import com.bca.byc.model.projection.PostContentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE " +
            "p.user.id IN " +
            "(SELECT u.appUserAttribute.id FROM AppUser u WHERE u.appUserAttribute.id IN " +
            "(SELECT ua.id FROM AppUserAttribute ua)) AND " +
            "LOWER(p.description) LIKE LOWER(:keyword) " +
            "ORDER BY function('RANDOM') ")
    Page<Post> findRandomPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.bca.byc.model.projection.impl.PostContentProjectionImpl(p.secureId, p.id, " +
            "MIN(pc.content), MIN(pc.type), MIN(pc.thumbnail)) " +
            "FROM Post p JOIN p.postContents pc " +
            "WHERE p.user.id = :userId AND LOWER(p.description) LIKE LOWER(:keyword) " +
            "GROUP BY p.id, p.secureId")
    Page<PostContentProjection> findMyPost(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.secureId = :secureId")
    Optional<IdSecureIdProjection> findByIdSecureId(@Param("secureId") String secureId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
            "(SELECT u.id FROM AppUser u JOIN u.follows f WHERE :userId = f.id)" +
            "AND LOWER (p.description) LIKE LOWER(:tag) ")
    Page<Post> findLatestPostsFromFollowingUsers(@Param("userId") Long userId, @Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    Optional<Post> findBySecureId(@Param("userId") String secureId);

    boolean existsBySecureId(String secureId);

    void deleteBySecureId(String secureId);
}
