package com.bca.byc.repository.auth;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.UserActivityCounts;
import com.bca.byc.model.projection.IdEmailProjection;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u  FROM AppUser u WHERE u.email = :email")
    Optional<AppUser> findByEmail(@Param("email") String email);

    @Query("SELECT new com.bca.byc.model.projection.impl.IdEmailProjectionImpl(u.id, u.email) FROM AppUser u " +
            "WHERE u.email = :email")
    Optional<IdEmailProjection> findByIdInEmail(@Param("email") String email);

    @Query("SELECT u FROM AppUser u WHERE u.secureId = :userId")
    Optional<AppUser> findBySecureId(@Param("userId") String secureId);

    @Query("SELECT new com.bca.byc.model.projection.impl.IdSecureIdProjectionImpl(u.id, u.secureId) " +
            "FROM AppUser u WHERE u.secureId = :id")
    Optional<IdSecureIdProjection> findUserBySecureId(@Param("id") String id);


    List<AppUser> findBySecureIdIn(List<String> secureIds);

    //    @Query(value = "SELECT au, aud, aua FROM AppUser au " +
//            "LEFT JOIN au.appUserDetail aud ON aud.id = au.appUserDetail.id " +
//            "LEFT JOIN au.appUserAttribute aua ON aua.id = au.appUserAttribute.id " +
//            "WHERE aud.status = 6")
    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsRecommendedTrue(String userName, StatusType statusType, Pageable pageable);

    Page<AppUser> findByNameLikeIgnoreCase(String name, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT p) AS totalPosts, " +
            "COUNT(DISTINCT f) AS totalFollowing, " +
            "COUNT(DISTINCT fw) AS totalFollowers " +
            "FROM AppUser u " +
            "LEFT JOIN u.posts p " +
            "LEFT JOIN u.follows f " +
            "LEFT JOIN u.followers fw " +
            "WHERE u.secureId = :secureId")
    UserActivityCounts getActivityCounts(@Param("secureId") String secureId);



    @Query("SELECT u FROM AppUser u " + "JOIN u.savedPosts us " + "JOIN us.post p " + "WHERE u.secureId = :userId " + "GROUP BY u")
    Page<AppUser> showProfileSavedActivity(String userId, Pageable pageable);

    @Query("SELECT u FROM AppUser u " + "JOIN u.likesPosts lp " + "JOIN lp.post p " + "WHERE u.secureId = :userId " + "GROUP BY u")
    Page<AppUser> showProfileLikesActivity(String userId, Pageable pageable);

}

