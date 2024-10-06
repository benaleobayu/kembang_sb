package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BusinessCatalogRepository extends JpaRepository<BusinessCatalog, Long> {
    Optional<BusinessCatalog> findBySecureId(String secureId);

    Page<BusinessCatalog> findByBusiness(Business business, Pageable pageable);

    void deleteByBusiness(Business business);


    @Query("SELECT bc FROM BusinessCatalog bc " +
            "JOIN Business b ON bc.business.id = b.id " +
            "WHERE " +
            "(LOWER(bc.title) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "b.id = :id")
    Page<BusinessCatalog> findByTitleLikeIgnoreCase(Long id, String keyword, Pageable pageable);
}
