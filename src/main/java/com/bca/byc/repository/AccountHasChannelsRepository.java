package com.bca.byc.repository;

import com.bca.byc.entity.AccountHasChannels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountHasChannelsRepository extends JpaRepository<AccountHasChannels, Long> {

    void deleteByAccountId(Long id);

    @Query("""
            SELECT c.channel.id FROM AccountHasChannels c WHERE c.account.id = :id
            """)
    List<Long> findChannelIdsByAccountId(@Param("id") Long id);
}