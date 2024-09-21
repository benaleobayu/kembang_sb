package com.bca.byc.repository;

import com.bca.byc.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {


    Optional<Tag> findByName(String tagName);

    Page<Tag> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}
