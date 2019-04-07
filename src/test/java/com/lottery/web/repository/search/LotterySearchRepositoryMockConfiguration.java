package com.lottery.web.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of LotterySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LotterySearchRepositoryMockConfiguration {

    @MockBean
    private LotterySearchRepository mockLotterySearchRepository;

}
