package com.bca.byc.repository;

import com.bca.byc.entity.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Optional<Settings> findByIdentity(String identity);

    Page<Settings> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}
