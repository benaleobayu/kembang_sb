package com.bca.byc.repository.Elastic;

import com.bca.byc.entity.elastic.BranchElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchElasticRepository extends ElasticsearchRepository<BranchElastic, Long> {


}
