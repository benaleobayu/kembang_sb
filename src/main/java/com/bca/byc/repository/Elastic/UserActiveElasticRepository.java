package com.bca.byc.repository.Elastic;

import com.bca.byc.model.Elastic.UserActiveElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActiveElasticRepository extends ElasticsearchRepository<UserActiveElastic, Long> {

    Page<UserActiveElastic> findAllBy(Pageable pageable);
}
