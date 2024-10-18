package com.bca.byc.repository;

import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.model.dto.AppUserDetailNameQueryDTO;
import com.bca.byc.model.dto.AppUserDetailPhoneQueryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectionRepository extends JpaRepository<AppUserDetail, Long> {

    @Query("""
            SELECT new com.bca.byc.model.dto.AppUserDetailNameQueryDTO(u.id, aud.name)
            FROM AppUser u
            LEFT JOIN u.appUserDetail aud
            WHERE u.id IN :id
            """)
    List<AppUserDetailNameQueryDTO> findUserNameByIdList(List<Long> id);

    @Query("""
            SELECT new com.bca.byc.model.dto.AppUserDetailPhoneQueryDTO(u.id, aud.phone)
            FROM AppUser u
            LEFT JOIN u.appUserDetail aud
            WHERE u.id IN :id
            """)
    List<AppUserDetailPhoneQueryDTO> findUserPhoneByIdList(List<Long> id);
}
