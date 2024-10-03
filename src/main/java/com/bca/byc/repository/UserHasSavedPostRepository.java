package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.UserHasSavedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserHasSavedPostRepository extends JpaRepository<UserHasSavedPost, Long> {

    @Query("SELECT sp FROM UserHasSavedPost sp WHERE sp.user.secureId = :userId")
    Page<UserHasSavedPost> findSavedPostByUserId(@Param("userId") String userId, Pageable pageable);

    UserHasSavedPost findByPostAndUser(Post post, AppUser user);
}
