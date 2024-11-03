package com.bca.byc.repository;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<AppUser, Long> {

    @Query("""
            SELECT u FROM AppUser u
            WHERE
            (LOWER(u.name) LIKE LOWER(:keyword)) AND
            (:location IS NULL OR u.location = :location) AND
            (:isSubscriber IS NULL OR u.isSubscribed = :isSubscriber)
            """)
    Page<AppUser> getCustomerIndex(@Param("keyword") String keyword,
                                   Pageable pageable,
                                   @Param("location") Long location,
                                   @Param("isSubscriber") Boolean isSubscriber);
}