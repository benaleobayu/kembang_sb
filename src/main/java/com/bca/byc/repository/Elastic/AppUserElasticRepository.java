package com.bca.byc.repository.Elastic;

import com.bca.byc.model.Elastic.AppUserElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserElasticRepository extends ElasticsearchRepository<AppUserElastic, Long> {

    Page<AppUserElastic> findAllBy(Pageable pageable);
}
