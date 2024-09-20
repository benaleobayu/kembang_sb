package com.bca.byc.repository;

import com.bca.byc.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    Page<PostCategory> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}
