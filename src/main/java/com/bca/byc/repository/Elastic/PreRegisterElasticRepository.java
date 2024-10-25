package com.bca.byc.repository.Elastic;

import com.bca.byc.entity.elastic.PreRegisterElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreRegisterElasticRepository extends ElasticsearchRepository<PreRegisterElastic, Long> {

    @Query("""
            {
            "bool": {
                "should": [
                { "query_string" : { "query" : "*?*", "fields" : [ "name" ] } },
                { "query_string" : { "query" : "*?*", "fields" : [ "email" ] } }
                ]
            }
            }
            """)
    Page<PreRegisterElastic> FindAllPreRegister(
            @Param("keyword") String keyword,
            Pageable pageable);
}
