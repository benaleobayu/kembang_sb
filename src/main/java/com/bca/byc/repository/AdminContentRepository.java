package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminContentRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE p.isAdminPost = true AND " +
            "p.isActive = true AND " +
            "(LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%') ))")
    Page<Post> findDataPostByAdmin(String keyword, Pageable pageable);
}
