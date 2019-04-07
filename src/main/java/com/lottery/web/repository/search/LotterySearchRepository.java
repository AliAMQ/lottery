package com.lottery.web.repository.search;

import com.lottery.web.domain.Lottery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Lottery entity.
 */
public interface LotterySearchRepository extends ElasticsearchRepository<Lottery, Long> {
}
