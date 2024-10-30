package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :id")
    void deletePostById(@Param("id") Long id);


    // ------------- show list post home -------------
    @Query("SELECT p FROM Post p " +
            "WHERE " +
            "LOWER(p.description) LIKE LOWER(:keyword) AND " +
            "p.isActive = true AND " +
            "p.isDeleted = false AND " +
            "p.isPosted = true AND " +
            "p.isAdminPost = false " +
            "ORDER BY function('RANDOM') ")
    Page<Post> findRandomPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.user.id IN " +
            "(SELECT f.id FROM AppUser u JOIN u.follows f WHERE u.id = :userId) AND " +
            "LOWER(p.description) LIKE LOWER(:keyword) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findPostByFollowingUsers(@Param("userId") Long userId,@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT p FROM Post p
            WHERE p.isAdminPost = true AND
            p.isActive = true AND p.isDeleted = false AND
            (LOWER(p.description)LIKE (:keyword))
            """)
    Page<Post> findPostByOfficialUsers(String keyword, Pageable pageable);
    // ------------- show list post home -------------

    // ------------- show list my post -------------
    @Query("SELECT p " +
            "FROM Post p JOIN p.postContents pc " +
            "WHERE " +
            "p.user.id = :creatorId " +
            "AND LOWER(p.description) LIKE LOWER(:keyword) " +
            "GROUP BY p.id, p.secureId")
    Page<Post> findMyPost(@Param("keyword") String keyword, Pageable pageable, @Param("creatorId") Long id);

    @Query("SELECT p FROM Post p " +
            "JOIN p.postContents pc " +
            "JOIN pc.tagUsers tu " +
            "WHERE tu.id = :creatorId")
    Page<Post> findTaggedPost(@Param("creatorId") Long creatorId, Pageable pageable);


//    @Query("SELECT new com.bca.byc.model.projection.impl.PostContentProjectionImpl(p.secureId, p.id, " +
//            "MIN(pc.content), MIN(pc.type), MIN(pc.thumbnail)) " +
//            "FROM Post p JOIN p.postContents pc " +
//            "WHERE p.user.id = :userId AND LOWER(p.description) LIKE LOWER(:keyword) " +
//            "GROUP BY p.id, p.secureId")
//    Page<Post> findMyPost(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    // ------------- show list my post -------------

    // ------------- search -------------
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN BusinessCategory bc ON bc.id = p.postCategory.id " +
            "WHERE " +
            "((LOWER(p.description) LIKE LOWER(:keyword) ) OR " +
            "(LOWER(bc.name) LIKE LOWER(:keyword) )) AND " +
            "p.isActive = true AND " +
            "bc.parentId is NULL")
    Page<Post> findPostByLobAndDescription(String keyword, Pageable pageable);
    // ------------- search -------------


    @Query("SELECT p FROM Post p WHERE p.secureId = :secureId")
    Optional<IdSecureIdProjection> findByIdSecureId(@Param("secureId") String secureId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
            "(SELECT u.id FROM AppUser u JOIN u.follows f WHERE :userId = f.id)" +
            "AND LOWER (p.description) LIKE LOWER(:tag) ")
    Page<Post> findLatestPostsFromFollowingUsers(@Param("userId") Long userId, @Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.secureId = :postId")
    Optional<Post> findBySecureId(@Param("postId") String secureId);

    boolean existsBySecureId(String secureId);

    void deleteBySecureId(String secureId);

    // ------------- post on channel -------------
    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN p.postContents pc " +
            "LEFT JOIN p.user u " +
            "WHERE " +
            "p.isAdminPost = true AND " +
            "(LOWER(p.description) LIKE LOWER(:keyword) ) AND " +
            "p.channel.id = :channelId ")
    Page<Post> findPostOnChannel(@Param("keyword") String keyword, Pageable pageable,@Param("channelId") Long channelId);

    @Query("""
            SELECT p FROM Post p
            WHERE p.isTeaser = true
            """)
    Post findDataPostTeaserByAdmin();

    // ------------- post on channel -------------

}
