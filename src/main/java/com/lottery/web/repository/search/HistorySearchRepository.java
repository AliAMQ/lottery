package com.lottery.web.repository.search;

import com.lottery.web.domain.History;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the History entity.
 */
public interface HistorySearchRepository extends ElasticsearchRepository<History, Long> {
}
