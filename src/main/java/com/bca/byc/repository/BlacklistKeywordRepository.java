package com.bca.byc.repository;

import com.bca.byc.entity.BlacklistKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistKeywordRepository extends JpaRepository<BlacklistKeyword, Long> {

    Page<BlacklistKeyword> findByKeywordLikeIgnoreCase(String keyword, Pageable pageable);

    Optional<BlacklistKeyword> findBySecureId(String secureId);
}
