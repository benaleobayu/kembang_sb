package com.bca.byc.repository;

import com.bca.byc.entity.Elastic.AppUserElastic;
import com.bca.byc.response.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AppUserElasticRepository extends ElasticsearchRepository<AppUserElastic, Long> {

    Page<AppUserElastic> findAllBy(Pageable pageable);
}
