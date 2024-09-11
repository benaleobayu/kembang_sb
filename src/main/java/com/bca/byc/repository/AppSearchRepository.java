package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSearchRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleLikeIgnoreCase(String userName, Pageable pageable);

    Post findByTitle(String email);
}
