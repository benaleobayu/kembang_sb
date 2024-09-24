package com.bca.byc.repository;

import com.bca.byc.entity.AppUserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserDetailRepository extends JpaRepository<AppUserDetail, Long> {




    // validator
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM AppUserDetail p " +
            "WHERE p.status =  6 AND p.memberCin = :memberCin")
    Boolean existsByMemberCinWithStatusApproved(@Param("memberCin") String cin);
}
