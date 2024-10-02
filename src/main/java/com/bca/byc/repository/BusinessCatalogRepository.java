package com.bca.byc.repository;

import com.bca.byc.entity.BusinessCatalog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.bca.byc.entity.Business;


@Repository
public interface BusinessCatalogRepository extends JpaRepository<BusinessCatalog, Long> {
    Optional<BusinessCatalog> findBySecureId(String secureId);
    Page<BusinessCatalog> findByBusiness(Business business, Pageable pageable);
    void deleteByBusiness(Business business);
}
