package com.lottery.web.repository.search;

import com.lottery.web.domain.Prize;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prize entity.
 */
public interface PrizeSearchRepository extends ElasticsearchRepository<Prize, Long> {
}
