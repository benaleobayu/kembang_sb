package com.bca.byc.repository;

import com.bca.byc.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminContentRepository extends JpaRepository<Post, Long> {
}
