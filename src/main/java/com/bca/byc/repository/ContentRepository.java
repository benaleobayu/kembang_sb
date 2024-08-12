package com.bca.byc.repository;

import com.bca.byc.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    boolean existsByName(String name);
}