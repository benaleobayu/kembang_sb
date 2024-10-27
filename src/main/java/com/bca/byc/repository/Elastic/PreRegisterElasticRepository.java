package com.bca.byc.repository.Elastic;

import com.bca.byc.entity.elastic.PreRegisterElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreRegisterElasticRepository extends ElasticsearchRepository<PreRegisterElastic, Long> {

    @Query("""
            {
               "bool": {
                 "must": [
                   {
                     "terms": {
                       "status_approval": ?5
                     }
                   }
                 ],
                 "should": [
                      { "query_string": { "query": "?0", "default_field": "email" }},
                      { "query_string": { "query": "?0", "default_field": "name" }}
                  ],
                 "filter": [
                   {
                     "range": {
                       "status_approval": {
                         "gte": "?3",
                         "lte": "?4"
                       }
                     }
                   },
                   {
                     "range": {
                       "created_at": {
                         "gte": "?1",
                         "lte": "?2"
                       }
                     }
                   }
                 ],
                   "minimum_should_match": 1
               }
             }
            """)
    Page<PreRegisterElastic> FindAllPreRegister(
            String keyword,
            String startDate,
            String endDate,
            String startStatus,
            String endStatus,
            List<String> listStatus,
            Pageable pageable);
}
