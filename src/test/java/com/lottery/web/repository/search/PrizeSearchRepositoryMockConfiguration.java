package com.lottery.web.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PrizeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PrizeSearchRepositoryMockConfiguration {

    @MockBean
    private PrizeSearchRepository mockPrizeSearchRepository;

}
